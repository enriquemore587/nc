package com.bbva.intranet.senders.utilities;

import com.bbva.intranet.data.exceptions.GAEAuthorizationException;
import com.bbva.intranet.senders.exceptions.SenderException;
import com.fga.exceptions.OAuthClientException;
import com.fga.oauth.client.utils.OAuthResponse;
import com.google.api.client.http.HttpStatusCodes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.Charset;

public abstract class GNUtil {

    public static final Logger LOG = LoggerFactory.getLogger(GNUtil.class);

    private static final String FORBIDDEN_ERROR = "FORBIDDEN ERROR";
    private static final String BAD_REQUEST_ERROR = "BAD REQUEST ERROR";
    private static final String NOT_FOUND_ERROR = "NOT FOUND ERROR";
    private static final String INTERNAL_SERVER_ERROR = "INTERNAL SERVER ERROR";
    private static final String SERVICE_UNAVAILABLE_ERROR = "SERVICE UNAVAILABLE ERROR";
    private static final String SERVICE_UNAUTHORIZED = "SERVICE UNAUTHORIZED";

    public static <T> T buildResponse(OAuthResponse oAuthResponse, Class<T> clazz) throws SenderException {
        if (clazz != null) {
            String json = checkResponse(oAuthResponse);
            Gson gson = new GsonBuilder().create();
            return gson.fromJson(json, clazz);
        }
        else
            throw new SenderException("clazz is null");
    }

    public static String checkResponse(OAuthResponse oAuthResponse) throws SenderException {
        String json = new String(oAuthResponse.getContentBytes(), Charset.forName(oAuthResponse.getCharset()));
        LOG.info(String.format("GN | Response to check. CODE: [%s] -  JSON: [%s]", oAuthResponse.getStatusCode(), json));
        switch (oAuthResponse.getStatusCode()) {
            case HttpStatusCodes.STATUS_CODE_OK:
                LOG.info(String.format("GN | Success response. Code [%s] - [%s]", HttpStatusCodes.STATUS_CODE_OK, json));
                break;
            case HttpStatusCodes.STATUS_CODE_CREATED:
                LOG.info(String.format("GN | Success response. Code [%s] - [%s]", HttpStatusCodes.STATUS_CODE_OK, json));
                break;
            case HttpStatusCodes.STATUS_CODE_FORBIDDEN:
                throw new SenderException(String.format("GN | [%s] - [%s]", FORBIDDEN_ERROR, json));
            case HttpStatusCodes.STATUS_CODE_BAD_REQUEST:
                throw new SenderException(String.format("GN | [%s] - [%s]", BAD_REQUEST_ERROR, json));
            case HttpStatusCodes.STATUS_CODE_SERVICE_UNAVAILABLE:
                throw new SenderException(String.format("GN | [%s] - [%s]", SERVICE_UNAVAILABLE_ERROR, json));
            case HttpStatusCodes.STATUS_CODE_NOT_FOUND:
                throw new SenderException(String.format("GN | [%s] - [%s]", NOT_FOUND_ERROR, json));
            case HttpStatusCodes.STATUS_CODE_SERVER_ERROR:
                throw new SenderException(String.format("GN | [%s] - [%s]", INTERNAL_SERVER_ERROR, json));
            case HttpStatusCodes.STATUS_CODE_UNAUTHORIZED:
                throw new SenderException(String.format("GN | [%s] - [%s]", SERVICE_UNAUTHORIZED, json));
            default:
                throw new SenderException(String.format("GN | DEFAULT ERROR - HTTP CODE [%s] - [%s]", oAuthResponse.getStatusCode(), json));
        }
        return json;
    }

    public static String exceptionMessageToPrint(String dataSent, OAuthClientException e) {
        return String.format("GN | call failed, data sent:  %s , code error:  [%s] , error:  %s ", dataSent, e.getCode(), e.getMessage());
    }

}
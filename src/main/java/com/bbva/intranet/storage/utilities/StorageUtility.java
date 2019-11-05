package com.bbva.intranet.storage.utilities;

import com.bbva.intranet.storage.exceptions.StorageException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class StorageUtility {

    public static Logger LOG = LoggerFactory.getLogger(StorageUtility.class);

    public static final String UNAUTHORIZED_ERROR = "Unauthorized service | NS";
    public static final String BAD_REQUEST_ERROR = "Forbidden service | NS";
    public static final String NOT_FOUND_ERROR = "Not found service | NS";
    public static final String INTERNAL_SERVER_ERROR = "Internal service error | NS";
    public static final String CONFLICT_SERVER_ERROR = "Conflict service error | NS";
    public static final String SERVICE_UNAVAILABLE_ERROR = "SERVICE UNAVAILABLE | NS";
    public static final String SUCCESS = "SUCCESS | NS";

    public static final String LOG_STARTING = "Starting";
    public static final String LOG_FINISHED = "Finished";


    public static String buildMsg(String msg, int statusCode, String jsonString) {
        return String.format("WS Response: [%s] - HTTP STATUS CODE: [%s] - BODY: [%s]", msg, statusCode, jsonString);
    }

    public static String buildExceptionMsg(String msg, int statusCode) {
        return String.format("[%s] - HTTP STATUS CODE: [%s]", msg, statusCode);
    }

    public static <T> T buildResponse(HttpResponse response, Class<T> valueType) throws StorageException, IOException {
        checkResponse(response);
        HttpEntity result = response.getEntity();
        if (result != null) {
            String jsonString = null;
            jsonString = EntityUtils.toString(result);
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(jsonString, valueType);
        }
        return null;
    }

    public static void checkResponse(HttpResponse response) throws StorageException {
        HttpEntity result = response.getEntity();
        String jsonString = null;
        int statusCode = response.getStatusLine().getStatusCode();
        try {
            jsonString = EntityUtils.toString(result);
        } catch (IOException e) {
            jsonString = String.format("Can't read body response: [%s]", e.getMessage());
            LOG.error(jsonString);
        }
        switch (statusCode) {
            case BadRequest.code:
                LOG.info(buildMsg(BAD_REQUEST_ERROR, statusCode, jsonString));
                throw new BadRequest(buildExceptionMsg(BAD_REQUEST_ERROR, statusCode), statusCode);

            case NotFound.code:
                LOG.info(buildMsg(BAD_REQUEST_ERROR, statusCode, jsonString));
                throw new BadRequest(buildExceptionMsg(NOT_FOUND_ERROR, statusCode), statusCode);

            case Unauthorized.code:
                LOG.info(buildMsg(BAD_REQUEST_ERROR, statusCode, jsonString));
                throw new Unauthorized(buildExceptionMsg(UNAUTHORIZED_ERROR, statusCode), statusCode);

            case Conflict.code:
                LOG.info(buildMsg(BAD_REQUEST_ERROR, statusCode, jsonString));
                throw new Conflict(buildExceptionMsg(CONFLICT_SERVER_ERROR, statusCode), statusCode);

            case ServiceUnavailable.code:
                LOG.info(buildMsg(BAD_REQUEST_ERROR, statusCode, jsonString));
                throw new ServiceUnavailable(buildExceptionMsg(SERVICE_UNAVAILABLE_ERROR, statusCode), statusCode);

            case InternalServerError.code:
                LOG.info(buildMsg(BAD_REQUEST_ERROR, statusCode, jsonString));
                throw new BadRequest(buildExceptionMsg(INTERNAL_SERVER_ERROR, statusCode), statusCode);

            case 200:
                LOG.info(buildMsg(SUCCESS, statusCode, jsonString));
                break;

            case 201:
                LOG.info(buildMsg(SUCCESS, statusCode, jsonString));
                break;

            default:
                LOG.info(buildMsg(BAD_REQUEST_ERROR, statusCode, jsonString));
                throw new UnControledResponse(buildExceptionMsg(UnControledResponse.class.getSimpleName(), statusCode), statusCode);
        }
    }

    public static class BadRequest extends StorageException {
        public final static int code = 400;
        /**
         * Constructs a new exception with {@code null} as its detail message.
         * The cause is not initialized, and may subsequently be initialized by a
         * call to {@link #initCause}.
         *
         * @param message
         * @param statusCode
         */
        public BadRequest(String message, int statusCode) {
            super(message, statusCode);
        }
    }

    public static class NotFound extends StorageException {
        public final static int code = 404;
        /**
         * Constructs a new exception with {@code null} as its detail message.
         * The cause is not initialized, and may subsequently be initialized by a
         * call to {@link #initCause}.
         *
         * @param message
         * @param statusCode
         */
        public NotFound(String message, int statusCode) {
            super(message, statusCode);
        }
    }

    public static class Unauthorized extends StorageException {
        public final static int code = 401;

        /**
         * Constructs a new exception with {@code null} as its detail message.
         * The cause is not initialized, and may subsequently be initialized by a
         * call to {@link #initCause}.
         *
         * @param message
         * @param statusCode
         */
        public Unauthorized(String message, Integer statusCode) {
            super(message, statusCode);
        }
    }

    public static class Conflict extends StorageException {
        public final static int code = 409;

        /**
         * Constructs a new exception with {@code null} as its detail message.
         * The cause is not initialized, and may subsequently be initialized by a
         * call to {@link #initCause}.
         *
         * @param message
         * @param statusCode
         */
        public Conflict(String message, int statusCode) {
            super(message, statusCode);
        }
    }

    public static class ServiceUnavailable extends StorageException {
        public final static int code = 503;

        /**
         * Constructs a new exception with {@code null} as its detail message.
         * The cause is not initialized, and may subsequently be initialized by a
         * call to {@link #initCause}.
         *
         * @param message
         * @param statusCode
         */
        public ServiceUnavailable(String message, int statusCode) {
            super(message, statusCode);
        }
    }

    public static class InternalServerError extends StorageException {
        public final static int code = 500;

        /**
         * Constructs a new exception with {@code null} as its detail message.
         * The cause is not initialized, and may subsequently be initialized by a
         * call to {@link #initCause}.
         *
         * @param message
         * @param statusCode
         */
        public InternalServerError(String message, int statusCode) {
            super(message, statusCode);
        }
    }

    public static class UnControledResponse extends StorageException{

        /**
         * Constructs a new exception with {@code null} as its detail message.
         * The cause is not initialized, and may subsequently be initialized by a
         * call to {@link #initCause}.
         *
         * @param message
         * @param statusCode
         */
        public UnControledResponse(String message, int statusCode) {
            super(message, statusCode);
        }
    }

}

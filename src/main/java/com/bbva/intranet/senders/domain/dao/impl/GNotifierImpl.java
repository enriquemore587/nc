package com.bbva.intranet.senders.domain.dao.impl;

import com.bbva.intranet.senders.domain.dao.Sender;
import com.bbva.intranet.senders.domain.requests.UserToSubscribe;
import com.bbva.intranet.senders.domain.requests.UserToUnSubscribe;
import com.bbva.intranet.senders.domain.requests.desregister.Desregister;
import com.bbva.intranet.senders.domain.requests.notifications.PushNotification;
import com.bbva.intranet.senders.domain.requests.register.UserDeviceRegister;
import com.bbva.intranet.senders.domain.requests.topics.Topic;
import com.bbva.intranet.senders.domain.responses.GNotifier;
import com.bbva.intranet.senders.domain.responses.TopicResp;
import com.bbva.intranet.senders.exceptions.GNotifierException;
import com.bbva.intranet.senders.exceptions.SenderException;
import com.bbva.intranet.senders.utilities.GNUtil;
import com.fga.exceptions.OAuthClientException;
import com.fga.oauth.client.GatewayClient;
import com.fga.oauth.client.utils.OAuthResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static com.bbva.intranet.not.core.utilities.NotCoreUtility.NC_SENDER_EMAIL;
import static com.bbva.intranet.not.core.utilities.NotCoreUtility.NC_SENDER_ID;
import static com.bbva.intranet.not.core.utilities.NotCoreUtility.STARTING;
import static com.bbva.intranet.not.core.utilities.NotCoreUtility.FINISHED;

public class GNotifierImpl implements Sender {

    private static Logger LOG = LoggerFactory.getLogger(GNotifierImpl.class);

    @Override
    public void register(UserDeviceRegister userDeviceRegister) throws SenderException {
        LOG.info(STARTING);
        GatewayClient client = new GatewayClient(NC_SENDER_EMAIL);
        client.setThrowExceptionOnExecuteError(false);
        client.getHeaders().setContentType("application/json; charset=UTF-8");
        client.setEncodeQueryParams(true);
        String jsonData = GNUtil.objectToJson(userDeviceRegister);
        try {
            String servicePath = "/register";
            LOG.info(String.format("sender email: %s, Method: POST, URL: %s, BODY: %s", NC_SENDER_EMAIL, servicePath, jsonData));
            OAuthResponse oAuthResponse = client.doPost("gnotifier", servicePath, null, jsonData.getBytes());
            if (oAuthResponse == null) throw new GNotifierException("oAuthResponse is null.");
            else GNUtil.checkResponse(oAuthResponse);
        } catch (OAuthClientException e) {
            LOG.error(GNUtil.exceptionMessageToPrint(jsonData, e));
            throw new GNotifierException(e.getMessage(), e.getCode());
        }
        LOG.info(FINISHED);
    }

    @Override
    public void desRegister(Desregister desregister) throws SenderException {
        LOG.info(STARTING);
        GatewayClient client = new GatewayClient(NC_SENDER_EMAIL);
        client.setThrowExceptionOnExecuteError(false);
        client.getHeaders().setContentType("application/json; charset=UTF-8");
        client.setEncodeQueryParams(true);
        String jsonData = GNUtil.objectToJson(desregister);
        try {
            String servicePath = "/deregister";
            LOG.info(String.format("sender email: %s, Method: POST, URL: %s, BODY: %s", NC_SENDER_EMAIL, servicePath, jsonData));
            OAuthResponse oAuthResponse = client.doPost("gnotifier", servicePath, null, jsonData.getBytes());
            if (oAuthResponse == null) throw new GNotifierException("oAuthResponse is null.");
            else GNUtil.checkResponse(oAuthResponse);
        } catch (OAuthClientException e) {
            LOG.error(GNUtil.exceptionMessageToPrint(jsonData, e));
            throw new GNotifierException(e.getMessage(), e.getCode());
        }
        LOG.info(FINISHED);
    }

    @Override
    public void sendNotification(PushNotification pushNotification) throws SenderException {
        LOG.info(STARTING);
        GatewayClient client = new GatewayClient(NC_SENDER_EMAIL);
        client.setThrowExceptionOnExecuteError(false);
        client.getHeaders().setContentType("application/json; charset=UTF-8");
        client.setEncodeQueryParams(true);
        String jsonData = GNUtil.objectToJson(pushNotification);
        try {
            String servicePath = "/notifications";
            LOG.info(String.format("sender email: %s, Method: POST, URL: %s, BODY: %s", NC_SENDER_EMAIL, servicePath, jsonData));
            OAuthResponse oAuthResponse = client.doPost("gnotifier", servicePath, null, jsonData.getBytes());
            if (oAuthResponse == null) throw new GNotifierException("GN | oAuthResponse is null");
            else GNUtil.checkResponse(oAuthResponse);
        } catch (OAuthClientException e) {
            LOG.error(GNUtil.exceptionMessageToPrint(jsonData, e));
            throw new GNotifierException(e.getMessage(), e.getCode());
        }
        LOG.info(FINISHED);
    }

    @Override
    public void multiSendNotification(Set<PushNotification> notifications) throws SenderException {
        LOG.info(STARTING);
        int count = 0;
        for (PushNotification notification : notifications) {
            try {
                sendNotification(notification);
            } catch (SenderException e) {
                count++;
            }
        }
        if (count == notifications.size()) throw new SenderException("GN | All notifications failed");
        LOG.info(String.format("%s notifications failed from %s", count, notifications.size()));
        LOG.info(FINISHED);
    }

    @Override
    public TopicResp topicsBySenderEmail(Integer pageSize, Integer paginationKey) throws SenderException {
        LOG.info(STARTING);
        TopicResp topicResp = null;
        GatewayClient client = new GatewayClient(NC_SENDER_EMAIL);
        client.setThrowExceptionOnExecuteError(false);
        client.getHeaders().setContentType("application/json; charset=UTF-8");
        client.setEncodeQueryParams(true);
        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("pageSize", pageSize.toString());
        queryParams.put("paginationKey", paginationKey.toString());
        try {
            String servicePath = "/topics";
            LOG.info(String.format("sender email: %s, Method: GET, URL: %s, QUERY_PARAMS: [%s]", NC_SENDER_EMAIL, servicePath, queryParams));
            OAuthResponse oAuthResponse = client.doGet("gnotifier", servicePath, queryParams);
            if (oAuthResponse == null) throw new GNotifierException("oAuthResponse is null.");
            else {
                GNotifier response = (GNotifier) GNUtil.buildResponse(oAuthResponse, GNotifier.class);
                topicResp = (TopicResp) response.getData();
            }
        } catch (OAuthClientException e) {
            LOG.error(GNUtil.exceptionMessageToPrint(queryParams.toString(), e));
            throw new GNotifierException(e.getMessage(), e.getCode());
        }
        LOG.info(FINISHED);
        return topicResp;
    }

    @Override
    public void createTopic(Topic topic) throws SenderException {
        LOG.info(STARTING);
        GatewayClient client = new GatewayClient(NC_SENDER_EMAIL);
        client.setThrowExceptionOnExecuteError(false);
        client.getHeaders().setContentType("application/json; charset=UTF-8");
        client.setEncodeQueryParams(true);
        topic.setSender_id(NC_SENDER_ID);
        String jsonData = GNUtil.objectToJson(topic);
        try {
            String servicePath = "/topics";
            LOG.info(String.format("sender email: %s, Method: POST, URL: %s, BODY: %s", NC_SENDER_EMAIL, servicePath, jsonData));
            OAuthResponse oAuthResponse = client.doPost("gnotifier", servicePath, null, jsonData.getBytes());
            if (oAuthResponse == null) throw new GNotifierException("GN | oAuthResponse is null.");
            else GNUtil.checkResponse(oAuthResponse);
        } catch (OAuthClientException e) {
            LOG.error(GNUtil.exceptionMessageToPrint(jsonData, e));
            throw new GNotifierException(e.getMessage(), e.getCode());
        }
        LOG.info(FINISHED);
    }

    @Override
    public void deleteTopic(String topicName) throws SenderException {
        LOG.info(STARTING);
        GatewayClient client = new GatewayClient(NC_SENDER_EMAIL);
        client.setThrowExceptionOnExecuteError(false);
        client.getHeaders().setContentType("application/json; charset=UTF-8");
        client.setEncodeQueryParams(true);
        String servicePath = "";
        try {
            servicePath = String.format("/topics/%s", topicName);
            LOG.info(String.format("sender email %s, Method: DELETE, URL: %s", NC_SENDER_EMAIL, servicePath));
            OAuthResponse oAuthResponse = client.doDelete("gnotifier", servicePath, null);
            if (oAuthResponse == null) throw new GNotifierException("GN | oAuthResponse is null.");
            else GNUtil.checkResponse(oAuthResponse);
        } catch (OAuthClientException e) {
            LOG.error(GNUtil.exceptionMessageToPrint(topicName, e));
            throw new GNotifierException(e.getMessage(), e.getCode());
        }
        LOG.info(FINISHED);
    }

    @Override
    public void updateTopic(String topicName, Topic topic) throws SenderException {
        LOG.info(STARTING);
        GatewayClient client = new GatewayClient(NC_SENDER_EMAIL);
        client.setThrowExceptionOnExecuteError(false);
        client.getHeaders().setContentType("application/json; charset=UTF-8");
        client.setEncodeQueryParams(true);
        String jsonData = GNUtil.objectToJson(topic);
        try {
            String servicePath = String.format("/topics/%s", topicName);
            LOG.info(String.format("sender email: %s, Method: PUT, URL: %s, BODY: [%s]", NC_SENDER_EMAIL, servicePath, jsonData));
            OAuthResponse oAuthResponse = client.doPut("gnotifier", servicePath, null, jsonData.getBytes());
            if (oAuthResponse == null) throw new GNotifierException("GN | oAuthResponse is null.");
            else GNUtil.checkResponse(oAuthResponse);
        } catch (OAuthClientException e) {
            LOG.error(GNUtil.exceptionMessageToPrint(jsonData, e));
            throw new GNotifierException(e.getMessage(), e.getCode());
        }
        LOG.info(FINISHED);
    }

    @Override
    public void subscribeUserIntoTopic(String topicName, UserToSubscribe userToSubscribe) throws SenderException {
        LOG.info(STARTING);
        GatewayClient client = new GatewayClient(NC_SENDER_EMAIL);
        client.setThrowExceptionOnExecuteError(false);
        client.getHeaders().setContentType("application/json; charset=UTF-8");
        client.setEncodeQueryParams(true);
        String jsonData = GNUtil.objectToJson(userToSubscribe);
        try {
            String servicePath = String.format("/topics/%s/users", topicName);
            LOG.info(String.format("sender email: %s, Method: POST, URL: %s, BODY: %s", NC_SENDER_EMAIL, servicePath, jsonData));
            OAuthResponse oAuthResponse = client.doPost("gnotifier", servicePath, null, jsonData.getBytes());
            if (oAuthResponse == null) throw new GNotifierException("GN | oAuthResponse is null.");
            else GNUtil.checkResponse(oAuthResponse);
        } catch (OAuthClientException e) {
            LOG.error(GNUtil.exceptionMessageToPrint(jsonData, e));
            throw new GNotifierException(e.getMessage(), e.getCode());
        }
        LOG.info(FINISHED);
    }

    @Override
    public void unSubscribeUserIntoTopic(String topicName, UserToUnSubscribe userToUnSubscribe) throws SenderException {
        LOG.info(STARTING);
        GatewayClient client = new GatewayClient(NC_SENDER_EMAIL);
        client.setThrowExceptionOnExecuteError(false);
        client.getHeaders().setContentType("application/json; charset=UTF-8");
        client.setEncodeQueryParams(true);
        try {
            String servicePath = String.format("/topics/%s/users/%s", topicName, userToUnSubscribe.getUserId());
            Map<String, String> queryParams = new HashMap<>();
            queryParams.put("application_id", Long.toString(userToUnSubscribe.getApplicationId()));
            LOG.info(String.format("sender email: %s, Method: DELETE, URL: %s, QUERY_PARAMS: %s", NC_SENDER_EMAIL, servicePath, queryParams));
            OAuthResponse oAuthResponse = client.doDelete("gnotifier", servicePath, queryParams);
            if (oAuthResponse == null) throw new GNotifierException("GN | oAuthResponse is null.");
            else GNUtil.checkResponse(oAuthResponse);
        } catch (OAuthClientException e) {
            LOG.error(GNUtil.exceptionMessageToPrint("application_id : "+userToUnSubscribe.getApplicationId(), e));
            throw new GNotifierException(e.getMessage(), e.getCode());
        }
        LOG.info(FINISHED);
    }
}

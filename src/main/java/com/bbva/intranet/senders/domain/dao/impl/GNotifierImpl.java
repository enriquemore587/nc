package com.bbva.intranet.senders.domain.dao.impl;

import com.bbva.intranet.ns.utilities.NSUtility;
import com.bbva.intranet.senders.domain.dao.Sender;
import com.bbva.intranet.senders.domain.requests.UserToSubscribe;
import com.bbva.intranet.senders.domain.requests.UserToUnSubscribe;
import com.bbva.intranet.senders.domain.requests.notifications.PushNotification;
import com.bbva.intranet.senders.domain.requests.register.UserDeviceRegister;
import com.bbva.intranet.senders.domain.requests.topics.Topic;
import com.bbva.intranet.senders.domain.responses.GNotifier;
import com.bbva.intranet.senders.domain.responses.TopicResp;
import com.bbva.intranet.senders.exceptions.GNotifierException;
import com.bbva.intranet.senders.exceptions.SenderException;
import com.bbva.intranet.senders.utilities.GNUtil;
import com.bbva.intranet.utilities.GsonGAEUtility;
import com.fga.exceptions.OAuthClientException;
import com.fga.oauth.client.GatewayClient;
import com.fga.oauth.client.utils.OAuthResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Component
@Qualifier("gn")
public class GNotifierImpl implements Sender {

    public static Logger LOG = LoggerFactory.getLogger(GNotifierImpl.class);

    @Override
    public void register(UserDeviceRegister userDeviceRegister) throws SenderException {
        GatewayClient client = new GatewayClient(NSUtility.NS_SENDER_EMAIL);
        client.setThrowExceptionOnExecuteError(false);
        client.getHeaders().setContentType("application/json; charset=UTF-8");
        client.setEncodeQueryParams(true);
        String jsonData = GsonGAEUtility.objectToJson(userDeviceRegister);
        try {
            String servicePath = "/register";
            LOG.info(String.format("sender email: %s, Method: POST, URL: %s, BODY: %s", NSUtility.NS_SENDER_EMAIL, servicePath, jsonData));
            OAuthResponse oAuthResponse = client.doPost("gnotifier", servicePath, null, jsonData.getBytes());
            if (oAuthResponse == null) throw new SenderException("oAuthResponse is null.");
            else GNUtil.checkResponse(oAuthResponse);
        } catch (OAuthClientException e) {
            LOG.error(GNUtil.exceptionMessageToPrint(jsonData, e));
            throw new GNotifierException(String.format("GN | No was register"));
        }
    }

    @Override
    public void send(PushNotification pushNotification) throws SenderException {
        GatewayClient client = new GatewayClient(NSUtility.NS_SENDER_EMAIL);
        client.setThrowExceptionOnExecuteError(false);
        client.getHeaders().setContentType("application/json; charset=UTF-8");
        client.setEncodeQueryParams(true);
        String jsonData = GsonGAEUtility.objectToJson(pushNotification);
        try {
            String servicePath = "/notifications";
            LOG.info(String.format("sender email: %s, Method: POST, URL: %s, BODY: %s", NSUtility.NS_SENDER_EMAIL, servicePath, jsonData));
            OAuthResponse oAuthResponse = client.doPost("gnotifier", servicePath, null, jsonData.getBytes());
            if (oAuthResponse == null) throw new GNotifierException("GN | oAuthResponse is null");
            else GNUtil.checkResponse(oAuthResponse);
        } catch (OAuthClientException e) {
            LOG.error(GNUtil.exceptionMessageToPrint(jsonData, e));
            throw new GNotifierException(String.format("GN | Couldn't send notification"));
        }
    }

    @Override
    public void multiSend(Set<PushNotification> notifications) throws SenderException {
        int count = 0;
        for (PushNotification notification : notifications) {
            try {
                send(notification);
            } catch (SenderException e) {
                count++;
            }
        }
        if (count == notifications.size()) throw new SenderException("GN | All notifications failed");
        LOG.info(String.format("%s notifications failed from %s", count, notifications.size()));
    }

    @Override
    public TopicResp topicsBySenderEmail(Integer pageSize, Integer paginationKey) throws SenderException {
        GatewayClient client = new GatewayClient(NSUtility.NS_SENDER_EMAIL);
        client.setThrowExceptionOnExecuteError(false);
        client.getHeaders().setContentType("application/json; charset=UTF-8");
        client.setEncodeQueryParams(true);
        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("pageSize", pageSize.toString());
        queryParams.put("paginationKey", paginationKey.toString());
        try {
            String servicePath = "/topics";
            LOG.info(String.format("Method: GET, URL: %s, QUERY_PARAMS: [%s]", servicePath, queryParams));
            OAuthResponse oAuthResponse = client.doGet("gnotifier", servicePath, queryParams);
            if (oAuthResponse == null) throw new SenderException("oAuthResponse is null.");
            else {
                GNotifier response = (GNotifier) GNUtil.buildResponse(oAuthResponse, GNotifier.class);
                return (TopicResp) response.getData();
            }
        } catch (OAuthClientException e) {
            throw new SenderException(String.format("GN | Couldn't fetch results to %s", NSUtility.NS_SENDER_EMAIL));
        }
    }

    @Override
    public void createTopic(Topic topic) throws SenderException {
        GatewayClient client = new GatewayClient(NSUtility.NS_SENDER_EMAIL);
        client.setThrowExceptionOnExecuteError(false);
        client.getHeaders().setContentType("application/json; charset=UTF-8");
        client.setEncodeQueryParams(true);
        String jsonData = GsonGAEUtility.objectToJson(topic);
        try {
            String servicePath = "/topics";
            LOG.info(String.format("SenderEmail: %s, Method: POST, URL: %s, BODY: %s", NSUtility.NS_SENDER_EMAIL, servicePath, jsonData));
            OAuthResponse oAuthResponse = client.doPost("gnotifier", servicePath, null, jsonData.getBytes());
            if (oAuthResponse == null) throw new SenderException("GN | oAuthResponse is null.");
            else GNUtil.checkResponse(oAuthResponse);
        } catch (OAuthClientException e) {
            LOG.error(GNUtil.exceptionMessageToPrint(jsonData, e));
            throw new SenderException(String.format("GN | Couldn't create %s topic", topic.getName()));
        }
    }

    @Override
    public void deleteTopic(String topicName) throws SenderException {
        GatewayClient client = new GatewayClient(NSUtility.NS_SENDER_EMAIL);
        client.setThrowExceptionOnExecuteError(false);
        client.getHeaders().setContentType("application/json; charset=UTF-8");
        client.setEncodeQueryParams(true);
        String servicePath = "";
        try {
            servicePath = String.format("/topics/%s", topicName);
            LOG.info(String.format("Method: DELETE, URL: %s", servicePath));
            OAuthResponse oAuthResponse = client.doDelete("gnotifier", servicePath, null);
            if (oAuthResponse == null) throw new SenderException("GN | oAuthResponse is null.");
            else GNUtil.checkResponse(oAuthResponse);
        } catch (OAuthClientException e) {
            LOG.error(GNUtil.exceptionMessageToPrint(servicePath, e));
            throw new SenderException(String.format("GN | Couldn't delete %s topic", topicName));
        }
    }

    @Override
    public void updateTopic(String topicName, Topic topic) throws SenderException {
        GatewayClient client = new GatewayClient(NSUtility.NS_SENDER_EMAIL);
        client.setThrowExceptionOnExecuteError(false);
        client.getHeaders().setContentType("application/json; charset=UTF-8");
        client.setEncodeQueryParams(true);
        String jsonData = GsonGAEUtility.objectToJson(topic);
        try {
            String servicePath = String.format("/topics/%s", topicName);
            LOG.info(String.format("Method: PUT, URL: %s, BODY: [%s]", servicePath, jsonData));
            OAuthResponse oAuthResponse = client.doPut("gnotifier", servicePath, null, jsonData.getBytes());
            if (oAuthResponse == null) throw new SenderException("GN | oAuthResponse is null.");
            else GNUtil.checkResponse(oAuthResponse);
        } catch (OAuthClientException e) {
            LOG.error(GNUtil.exceptionMessageToPrint(jsonData, e));
            throw new SenderException(String.format("GN | Couldn't update %s topic", topic.getName()));
        }
    }

    @Override
    public void subscribeUserIntoTopic(String topicName, UserToSubscribe userToSubscribe) throws SenderException {
        GatewayClient client = new GatewayClient(NSUtility.NS_SENDER_EMAIL);
        client.setThrowExceptionOnExecuteError(false);
        client.getHeaders().setContentType("application/json; charset=UTF-8");
        client.setEncodeQueryParams(true);
        String jsonData = GsonGAEUtility.objectToJson(userToSubscribe);
        try {
            String servicePath = String.format("/topics/%s/users", topicName);
            LOG.info(String.format("SenderEmail: %s, Method: POST, URL: %s, BODY: %s", NSUtility.NS_SENDER_EMAIL, servicePath, jsonData));
            OAuthResponse oAuthResponse = client.doPost("gnotifier", servicePath, null, jsonData.getBytes());
            if (oAuthResponse == null) throw new SenderException("GN | oAuthResponse is null.");
            else GNUtil.checkResponse(oAuthResponse);
        } catch (OAuthClientException e) {
            LOG.error(GNUtil.exceptionMessageToPrint(jsonData, e));
            throw new SenderException(String.format("GN | Couldn't subscribe %s into %s topic", userToSubscribe.getUser(), topicName));
        }
    }

    @Override
    public void unSubscribeUserIntoTopic(String topicName, UserToUnSubscribe userToUnSubscribe) throws SenderException {
        GatewayClient client = new GatewayClient(NSUtility.NS_SENDER_EMAIL);
        client.setThrowExceptionOnExecuteError(false);
        client.getHeaders().setContentType("application/json; charset=UTF-8");
        client.setEncodeQueryParams(true);
        try {
            String servicePath = String.format("/topics/%s/users/%s", topicName, userToUnSubscribe.getUserId());
            Map<String, String> queryParams = new HashMap<>();
            queryParams.put("application_id", Long.toString(userToUnSubscribe.getApplicationId()));
            LOG.info(String.format("Method: DELETE, URL: %s, QUERY_PARAMS: %s", servicePath, queryParams));
            OAuthResponse oAuthResponse = client.doDelete("gnotifier", servicePath, queryParams);
            if (oAuthResponse == null) throw new SenderException("GN | oAuthResponse is null.");
            else GNUtil.checkResponse(oAuthResponse);
        } catch (OAuthClientException e) {
            LOG.error(GNUtil.exceptionMessageToPrint("application_id : "+userToUnSubscribe.getApplicationId(), e));
            throw new SenderException(String.format("GN | Couldn't remove %s from %s topic", userToUnSubscribe.getUserId(), topicName));
        }
    }
}

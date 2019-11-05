package com.bbva.intranet.senders.domain.dao.impl;

import com.bbva.intranet.senders.domain.dao.Sender;
import com.bbva.intranet.senders.domain.requests.UserToSubscribe;
import com.bbva.intranet.senders.domain.requests.UserToUnSubscribe;
import com.bbva.intranet.senders.domain.requests.notifications.PushNotification;
import com.bbva.intranet.senders.domain.requests.register.UserDeviceRegister;
import com.bbva.intranet.senders.domain.requests.topics.Topic;
import com.bbva.intranet.senders.domain.responses.TopicResp;
import com.bbva.intranet.senders.exceptions.SenderException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@Qualifier("fcm")
public class FCMImpl implements Sender {

    public static Logger LOG = LoggerFactory.getLogger(FCMImpl.class);

    @Override
    public void register(UserDeviceRegister userDeviceRegister) throws SenderException {

    }

    @Override
    public void send(PushNotification pushNotification) throws SenderException {

    }

    @Override
    public void multiSend(Set<PushNotification> notifications) throws SenderException {

    }

    @Override
    public TopicResp topicsBySenderEmail(Integer pageSize, Integer paginationKey) throws SenderException {
        return null;
    }

    @Override
    public void createTopic(Topic topic) throws SenderException {

    }

    @Override
    public void deleteTopic(String topicName) throws SenderException {

    }

    @Override
    public void updateTopic(String topicName, Topic topic) throws SenderException {

    }

    @Override
    public void subscribeUserIntoTopic(String topicName, UserToSubscribe userToSubscribe) throws SenderException {

    }

    @Override
    public void unSubscribeUserIntoTopic(String topicName, UserToUnSubscribe userToUnSubscribe) throws SenderException {

    }

}

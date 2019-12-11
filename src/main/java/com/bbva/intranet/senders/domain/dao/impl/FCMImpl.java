package com.bbva.intranet.senders.domain.dao.impl;

import com.bbva.intranet.senders.domain.dao.Sender;
import com.bbva.intranet.senders.domain.requests.UserToSubscribe;
import com.bbva.intranet.senders.domain.requests.UserToUnSubscribe;
import com.bbva.intranet.senders.domain.requests.desregister.Desregister;
import com.bbva.intranet.senders.domain.requests.notifications.PushNotification;
import com.bbva.intranet.senders.domain.requests.register.UserDeviceRegister;
import com.bbva.intranet.senders.domain.requests.topics.Topic;
import com.bbva.intranet.senders.domain.responses.TopicResp;
import com.bbva.intranet.senders.exceptions.SenderException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

public class FCMImpl implements Sender {

    private static Logger LOG = LoggerFactory.getLogger(FCMImpl.class);

    @Override
    public void register(UserDeviceRegister userDeviceRegister) throws SenderException {

    }

    @Override
    public void desRegister(Desregister desregister) throws SenderException {

    }

    @Override
    public void sendNotification(PushNotification pushNotification) throws SenderException {

    }

    @Override
    public void multiSendNotification(Set<PushNotification> notifications) throws SenderException {

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

package com.bbva.intranet.not.core.dao.impl;

import com.bbva.intranet.not.core.dao.NotCore;
import com.bbva.intranet.not.core.exceptions.NotCoreException;
import com.bbva.intranet.not.core.utilities.NotCoreChannel;
import com.bbva.intranet.not.core.utilities.NotCoreUtility;
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

import static com.bbva.intranet.not.core.utilities.NotCoreUtility.FINISHED;
import static com.bbva.intranet.not.core.utilities.NotCoreUtility.STARTING;

public class NotCoreImpl implements NotCore {

    private static final Logger LOG = LoggerFactory.getLogger(NotCoreImpl.class);

    private Sender gnSender;

    private Sender fcmSender;

    public NotCoreImpl(Sender gnSender, Sender fcmSender) {
        this.gnSender = gnSender;
        this.fcmSender = fcmSender;
    }

    @Override
    public void register(NotCoreChannel channel, UserDeviceRegister deviceRegister) throws NotCoreException, SenderException {
        LOG.info(STARTING);
        NotCoreUtility.verifyChannel(channel);
        gnSender.register(deviceRegister);
        LOG.info(FINISHED);
    }

    @Override
    public void desRegister(NotCoreChannel channel, Desregister desregister) throws NotCoreException, SenderException {
        LOG.info(STARTING);
        NotCoreUtility.verifyChannel(channel);
        gnSender.desRegister(desregister);
        LOG.info(FINISHED);
    }

    @Override
    public void sendNotification(NotCoreChannel channel, PushNotification pushNotification) throws NotCoreException, SenderException {
        LOG.info(STARTING);
        NotCoreUtility.verifyChannel(channel);
        gnSender.sendNotification(pushNotification);
        LOG.info(FINISHED);
    }

    private TopicResp topicsBySenderEmail(NotCoreChannel channel, Integer pageSize, Integer paginationKey) throws NotCoreException, SenderException {
        LOG.info(STARTING);
        NotCoreUtility.verifyChannel(channel);
        TopicResp topicResp = gnSender.topicsBySenderEmail(pageSize, paginationKey);
        LOG.info(FINISHED);
        return topicResp;
    }

    @Override
    public void createTopic(NotCoreChannel channel, Topic topic) throws NotCoreException, SenderException {
        LOG.info(STARTING);
        NotCoreUtility.verifyChannel(channel);
        gnSender.createTopic(topic);
        LOG.info(FINISHED);
    }

    @Override
    public void deleteTopic(NotCoreChannel channel, String topicName) throws NotCoreException, SenderException {
        LOG.info(STARTING);
        NotCoreUtility.verifyChannel(channel);
        gnSender.deleteTopic(topicName);
        LOG.info(FINISHED);
    }

    @Override
    public void updateTopic(NotCoreChannel channel, String topicName, Topic topic) throws NotCoreException, SenderException {
        LOG.info(STARTING);
        NotCoreUtility.verifyChannel(channel);
        gnSender.updateTopic(topicName, topic);
        LOG.info(FINISHED);
    }

    @Override
    public void subscribeUserIntoTopic(NotCoreChannel channel, String topicName, UserToSubscribe userToSubscribe) throws NotCoreException, SenderException {
        LOG.info(STARTING);
        NotCoreUtility.verifyChannel(channel);
        gnSender.subscribeUserIntoTopic(topicName, userToSubscribe);
        LOG.info(FINISHED);
    }

    @Override
    public void unSubscribeUserIntoTopic(NotCoreChannel channel, String topicName, UserToUnSubscribe userToUnSubscribe) throws NotCoreException, SenderException {
        LOG.info(STARTING);
        NotCoreUtility.verifyChannel(channel);
        gnSender.unSubscribeUserIntoTopic(topicName, userToUnSubscribe);
        LOG.info(FINISHED);
    }
}

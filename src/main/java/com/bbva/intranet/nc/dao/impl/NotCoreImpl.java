package com.bbva.intranet.nc.dao.impl;

import com.bbva.intranet.nc.dao.NotCore;
import com.bbva.intranet.nc.exceptions.NotCoreException;
import com.bbva.intranet.nc.utilities.NotCoreChannel;
import com.bbva.intranet.nc.utilities.NotCoreUtility;
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

import java.util.Set;

import static com.bbva.intranet.nc.utilities.NotCoreUtility.FINISHED;
import static com.bbva.intranet.nc.utilities.NotCoreUtility.STARTING;

public class NotCoreImpl implements NotCore {

    public static Logger LOG = LoggerFactory.getLogger(NotCoreImpl.class);

    private Sender gnSender;

    private Sender fcmSender;

    public NotCoreImpl(Sender gnSender, Sender fcmSender) {
        this.gnSender = gnSender;
        this.fcmSender = fcmSender;
    }

    @Override
    public void register(NotCoreChannel channel, UserDeviceRegister deviceRegister) throws NotCoreException {
        LOG.info(STARTING);
        NotCoreUtility.verifyChannel(channel);
        try {
            gnSender.register(deviceRegister);
        } catch (SenderException e) {
            throw new NotCoreException(e.getMessage());
        }
        LOG.info(FINISHED);
    }

    @Override
    public void sendNotification(NotCoreChannel channel, PushNotification notification) throws NotCoreException {
        LOG.info(STARTING);
        NotCoreUtility.verifyChannel(channel);
        try {
            gnSender.sendNotification(notification);
        } catch (SenderException e) {
            throw new NotCoreException(e.getMessage());
        }
        LOG.info(FINISHED);
    }

    @Override
    public void multiSendNotification(NotCoreChannel channel, Set<PushNotification> notifications) throws NotCoreException {
        LOG.info(STARTING);
        NotCoreUtility.verifyChannel(channel);
        try {
            gnSender.multiSendNotification(notifications);
        } catch (SenderException e) {
            throw new NotCoreException(e.getMessage());
        }
        LOG.info(FINISHED);
    }

    private TopicResp topicsBySenderEmail(NotCoreChannel channel, Integer pageSize, Integer paginationKey) throws NotCoreException {
        LOG.info(STARTING);
        NotCoreUtility.verifyChannel(channel);
        TopicResp topicResp = null;
        try {
            topicResp = gnSender.topicsBySenderEmail(pageSize, paginationKey);
        } catch (SenderException e) {
            throw new NotCoreException(e.getMessage());
        }
        LOG.info(FINISHED);
        return topicResp;
    }

    @Override
    public void createTopic(NotCoreChannel channel, Topic topic) throws NotCoreException {
        LOG.info(STARTING);
        NotCoreUtility.verifyChannel(channel);
        try {
            gnSender.createTopic(topic);
        } catch (SenderException e) {
            throw new NotCoreException(e.getMessage());
        }
        LOG.info(FINISHED);
    }

    @Override
    public void deleteTopic(NotCoreChannel channel, String topicName) throws NotCoreException {
        LOG.info(STARTING);
        NotCoreUtility.verifyChannel(channel);
        try {
            gnSender.deleteTopic(topicName);
        } catch (SenderException e) {
            throw new NotCoreException(e.getMessage());
        }
        LOG.info(FINISHED);
    }

    @Override
    public void updateTopic(NotCoreChannel channel, String topicName, Topic topic) throws NotCoreException {
        LOG.info(STARTING);
        NotCoreUtility.verifyChannel(channel);
        try {
            gnSender.updateTopic(topicName, topic);
        } catch (SenderException e) {
            throw new NotCoreException(e.getMessage());
        }
        LOG.info(FINISHED);
    }

    @Override
    public void subscribeUserIntoTopic(NotCoreChannel channel, String topicName, UserToSubscribe userToSubscribe) throws NotCoreException {
        LOG.info(STARTING);
        NotCoreUtility.verifyChannel(channel);
        try {
            gnSender.subscribeUserIntoTopic(topicName, userToSubscribe);
        } catch (SenderException e) {
            throw new NotCoreException(e.getMessage());
        }
        LOG.info(FINISHED);
    }

    @Override
    public void unSubscribeUserIntoTopic(NotCoreChannel channel, String topicName, UserToUnSubscribe userToUnSubscribe) throws NotCoreException {
        LOG.info(STARTING);
        NotCoreUtility.verifyChannel(channel);
        try {
            gnSender.unSubscribeUserIntoTopic(topicName, userToUnSubscribe);
        } catch (SenderException e) {
            throw new NotCoreException(e.getMessage());
        }
        LOG.info(FINISHED);
    }
}

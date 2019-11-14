package com.bbva.intranet.not.core.dao.impl;

import com.bbva.intranet.not.core.dao.NotCore;
import com.bbva.intranet.not.core.exceptions.NotCoreException;
import com.bbva.intranet.not.core.exceptions.TransactionStoppedException;
import com.bbva.intranet.not.core.utilities.NotCoreChannel;
import com.bbva.intranet.not.core.utilities.NotCoreUtility;
import com.bbva.intranet.senders.domain.dao.Sender;
import com.bbva.intranet.senders.domain.requests.UserToSubscribe;
import com.bbva.intranet.senders.domain.requests.UserToUnSubscribe;
import com.bbva.intranet.senders.domain.requests.notifications.PushNotification;
import com.bbva.intranet.senders.domain.requests.register.UserDeviceRegister;
import com.bbva.intranet.senders.domain.requests.topics.Topic;
import com.bbva.intranet.senders.exceptions.SenderException;
import com.bbva.intranet.storage.domain.dao.*;
import com.bbva.intranet.storage.domain.model.DeviceM;
import com.bbva.intranet.storage.domain.model.UserM;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

public class NotCoreStorageImpl implements NotCore {

    private static final Logger LOG = LoggerFactory.getLogger(NotCoreStorageImpl.class);

    private Sender gnSender;

    private Sender fcmSender;
    private NotCoreDeviceDAO notCoreDeviceDAO;
    private NotCoreNotificationDAO notCoreNotificationDAO;
    private NotCoreNotificationSentDAO notCoreNotificationSentDAO;
    private NotCoreTopicDAO notCoreTopicDAO;
    private NotCoreUserDAO notCoreUserDAO;

    public NotCoreStorageImpl(Sender gnSender, Sender fcmSender, NotCoreDeviceDAO notCoreDeviceDAO, NotCoreNotificationDAO notCoreNotificationDAO, NotCoreNotificationSentDAO notCoreNotificationSentDAO, NotCoreTopicDAO notCoreTopicDAO, NotCoreUserDAO notCoreUserDAO) {
        this.gnSender = gnSender;
        this.fcmSender = fcmSender;
        this.notCoreDeviceDAO = notCoreDeviceDAO;
        this.notCoreNotificationDAO = notCoreNotificationDAO;
        this.notCoreNotificationSentDAO = notCoreNotificationSentDAO;
        this.notCoreTopicDAO = notCoreTopicDAO;
        this.notCoreUserDAO = notCoreUserDAO;
    }

    @Override
    public void register(NotCoreChannel channel, UserDeviceRegister deviceRegister) throws NotCoreException {
        NotCoreUtility.verifyChannel(channel);
        try {
            try {
                notCoreUserDAO.save(new UserM(deviceRegister.getUserId()));
            } catch (TransactionStoppedException e) {
                LOG.error(e.getMessage());
                throw new NotCoreException("Couldn't save new user.");
            }
            DeviceM deviceM = new DeviceM();
                deviceM.setBrand(deviceRegister.getDevice().getBrand());
                deviceM.setDeviceId(deviceRegister.getDevice().getId());
                deviceM.setLanguage(deviceRegister.getDevice().getLanguage());
                deviceM.setModel(deviceRegister.getDevice().getModel());
                deviceM.setOs(deviceRegister.getDevice().getOs());
                deviceM.setOsVersion(deviceRegister.getDevice().getOsVersion());
                deviceM.setToken(deviceRegister.getToken());
                deviceM.setUserId(deviceRegister.getUserId());
            notCoreDeviceDAO.save(deviceM);
            gnSender.register(deviceRegister);
        } catch (TransactionStoppedException e) {
            LOG.error(e.getMessage());
            throw new NotCoreException("Couldn't save new device");
        } catch (SenderException e) {
            LOG.error(e.getMessage());
            throw new NotCoreException("No was send notification.");
        }
    }

    @Override
    public void sendNotification(NotCoreChannel channel, PushNotification notification) throws NotCoreException {
        NotCoreUtility.verifyChannel(channel);


    }

    @Override
    public void multiSendNotification(NotCoreChannel channel, Set<PushNotification> notifications) throws NotCoreException {
        NotCoreUtility.verifyChannel(channel);
    }

    @Override
    public void createTopic(NotCoreChannel channel, Topic topic) throws NotCoreException {
        NotCoreUtility.verifyChannel(channel);
    }

    @Override
    public void deleteTopic(NotCoreChannel channel, String topicName) throws NotCoreException {
        NotCoreUtility.verifyChannel(channel);
    }

    @Override
    public void updateTopic(NotCoreChannel channel, String topicName, Topic topic) throws NotCoreException {
        NotCoreUtility.verifyChannel(channel);
    }

    @Override
    public void subscribeUserIntoTopic(NotCoreChannel channel, String topicName, UserToSubscribe userToSubscribe) throws NotCoreException {
        NotCoreUtility.verifyChannel(channel);
    }

    @Override
    public void unSubscribeUserIntoTopic(NotCoreChannel channel, String topicName, UserToUnSubscribe userToUnSubscribe) throws NotCoreException {
        NotCoreUtility.verifyChannel(channel);
    }
}
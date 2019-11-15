package com.bbva.intranet.not.core.dao.impl;

import com.bbva.intranet.not.core.dao.NotCore;
import com.bbva.intranet.not.core.exceptions.NoRecordFoundException;
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
import com.bbva.intranet.storage.domain.dao.NotCoreDeviceDAO;
import com.bbva.intranet.storage.domain.dao.NotCoreNotificationDAO;
import com.bbva.intranet.storage.domain.dao.NotCoreNotificationSentDAO;
import com.bbva.intranet.storage.domain.dao.NotCoreTopicDAO;
import com.bbva.intranet.storage.domain.dao.NotCoreUserDAO;
import com.bbva.intranet.storage.domain.model.DeviceM;
import com.bbva.intranet.storage.domain.model.NotificationM;
import com.bbva.intranet.storage.domain.model.NotificationSentM;
import com.bbva.intranet.storage.domain.model.TopicM;
import com.bbva.intranet.storage.domain.model.UserM;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

import static com.bbva.intranet.not.core.utilities.NotCoreUtility.UNCONTROLLED_ERROR_MESSAGE;

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
            this.notCoreUserDAO.findByEmail(deviceRegister.getUserId());
            throw new NotCoreException(String.format("%s is already exist", deviceRegister.getUserId()));
        } catch (NoRecordFoundException userNotExistException) {
            try {
                LOG.info(String.format("%s is not exist then will be store"));
                UserM userM = new UserM(deviceRegister.getUserId());
                notCoreUserDAO.save(userM);
                try {
                    //TODO: Hace falta validar que el dispositivo no exista ya.
                    DeviceM deviceM = new DeviceM();
                        deviceM.setBrand(deviceRegister.getDevice().getBrand());
                        deviceM.setDeviceId(deviceRegister.getDevice().getId());
                        deviceM.setLanguage(deviceRegister.getDevice().getLanguage());
                        deviceM.setModel(deviceRegister.getDevice().getModel());
                        deviceM.setOs(deviceRegister.getDevice().getOs());
                        deviceM.setOsVersion(deviceRegister.getDevice().getOsVersion());
                        deviceM.setToken(deviceRegister.getToken());
                        deviceM.setUserId(deviceRegister.getUserId());
                    LOG.info(String.format("The device with id %s will be saved for the user id %s", deviceM.getDeviceId(), deviceM.getUserId()));
                    notCoreDeviceDAO.save(deviceM);
                    LOG.info("GN will be call");
                    gnSender.register(deviceRegister);
                } catch (TransactionStoppedException e) {
                    throw new NotCoreException("Couldn't save device");
                } catch (SenderException e) {
                    throw new NotCoreException(e.getMessage());
                }
            } catch (TransactionStoppedException e) {
                LOG.error(e.getMessage());
                throw new NotCoreException("Couldn't store user");
            }
        } catch (TransactionStoppedException e) {
            throw new NotCoreException(e.getMessage());
        }
    }

    @Override
    public void sendNotification(NotCoreChannel channel, PushNotification pushNotification) throws NotCoreException {
        NotCoreUtility.verifyChannel(channel);
        NotificationM notificationM = null;
        UserM userM = null;
        Long templateId = pushNotification.getMessage().getTemplate().getId();
        try {
            notificationM = this.notCoreNotificationDAO.findBy(templateId);
            try {
                userM = this.notCoreUserDAO.findByEmail(pushNotification.getUserId());
            } catch (NoRecordFoundException e) {
                throw new NotCoreException(String.format("User with id «%s» not found ", pushNotification.getUserId()));
            }
        } catch (NoRecordFoundException e) {
            throw new NotCoreException(String.format("Notification with template id «%s» not found ", templateId));
        } catch (TransactionStoppedException e) {
            throw new NotCoreException(e.getMessage());
        }
        try {
            NotificationSentM notificationSentM = new NotificationSentM();
                notificationSentM.setNotificationId(notificationM.getId());
                notificationSentM.setRead(false);
                notificationSentM.setUserId(userM.getId());
            this.gnSender.sendNotification(pushNotification);
            this.notCoreNotificationSentDAO.save(notificationSentM);
        } catch (SenderException e) {
            throw new NotCoreException(e.getMessage());
        } catch (TransactionStoppedException e) {
            throw new NotCoreException(String.format("The notification couldn't stored but were sent successfully"));
        }
    }

    @Override
    public void multiSendNotification(NotCoreChannel channel, Set<PushNotification> notifications) throws NotCoreException {
        NotCoreUtility.verifyChannel(channel);
        try {
            LOG.info("Sending notifications");
            this.gnSender.multiSendNotification(notifications);
            int countErrors = 0;
            LOG.info("Storing notifications");
            for (PushNotification pushNotification : notifications) {
                NotificationM notificationM = null;
                UserM userM = null;
                Long templateId = pushNotification.getMessage().getTemplate().getId();
                try {
                    LOG.info(String.format("Fetching notification with template id «%s» from database", templateId));
                    notificationM = this.notCoreNotificationDAO.findBy(templateId);
                    try {
                        LOG.info(String.format("Fetching user with email «%s» from database", templateId));
                        userM = this.notCoreUserDAO.findByEmail(pushNotification.getUserId());
                    } catch (NoRecordFoundException e) {
                        countErrors++;
                        LOG.error(String.format("User with email «%s» not found ", pushNotification.getUserId()));
                        continue;
                    }
                } catch (NoRecordFoundException | TransactionStoppedException e) {
                    countErrors++;
                    LOG.error(e.getMessage());
                    continue;
                }
                try {
                    NotificationSentM notificationSentM = new NotificationSentM();
                    notificationSentM.setNotificationId(notificationM.getId());
                    notificationSentM.setRead(false);
                    notificationSentM.setUserId(userM.getId());
                    LOG.info(String.format("Notification storage with id %s that was sent to the user with the email %s", notificationSentM.getNotificationId() , notificationSentM.getUserId()));
                    this.notCoreNotificationSentDAO.save(notificationSentM);
                } catch (TransactionStoppedException e) {
                    countErrors++;
                    LOG.error(e.getMessage());
                    continue;
                }
            }
            if (countErrors == notifications.size()) throw new NotCoreException("The notifications could not be stored but were sent");
            LOG.info(String.format("%s no notifications was stored of %s.", countErrors, notifications.size()));
        } catch (SenderException e) {
            throw new NotCoreException(e.getMessage());
        }
    }

    @Override
    public void createTopic(NotCoreChannel channel, Topic topic) throws NotCoreException {
        NotCoreUtility.verifyChannel(channel);
        TopicM topicM = new TopicM();
        topicM.setName(topic.getName());
        topicM.setDescription(topic.getDescription());
        try {
            this.gnSender.createTopic(topic);
            this.notCoreTopicDAO.save(topicM);
        } catch (SenderException e) {
            throw new NotCoreException(e.getMessage());
        } catch (TransactionStoppedException e) {
            LOG.error(e.getMessage());
            throw new NotCoreException(String.format("The topic %s was not stored locally but was stored in GN", topic.getName()));
        }
    }

    @Override
    public void deleteTopic(NotCoreChannel channel, String topicName) throws NotCoreException {
        NotCoreUtility.verifyChannel(channel);
        try {
            TopicM topicM = this.notCoreTopicDAO.findByName(topicName);
            this.gnSender.deleteTopic(topicName);
            this.notCoreTopicDAO.delete(topicM);
        } catch (SenderException | NoRecordFoundException e) {
            throw new NotCoreException(e.getMessage());
        } catch (TransactionStoppedException e) {
            throw new NotCoreException(UNCONTROLLED_ERROR_MESSAGE);
        }
    }

    @Override
    public void updateTopic(NotCoreChannel channel, String topicName, Topic topic) throws NotCoreException {
        NotCoreUtility.verifyChannel(channel);
        try {
            TopicM topicM = this.notCoreTopicDAO.findByName(topicName);
            topicM.setName(topic.getName());
            topicM.setDescription(topic.getDescription());
            try {
                this.notCoreTopicDAO.update(topicM);
            } catch (TransactionStoppedException e) {
                throw new NotCoreException(String.format("«%s» couldn't store", topicName));
            }
        } catch (NoRecordFoundException e) {
            throw new NotCoreException(String.format("«%s» topic doesn't exist", topicName));
        } catch (TransactionStoppedException e) {
            throw new NotCoreException(e.getMessage());
        }
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
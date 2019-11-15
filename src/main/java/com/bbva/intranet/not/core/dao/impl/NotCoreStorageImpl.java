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
import com.bbva.intranet.storage.domain.dao.NotCoreUserTopicDAO;
import com.bbva.intranet.storage.domain.model.DeviceM;
import com.bbva.intranet.storage.domain.model.NotificationM;
import com.bbva.intranet.storage.domain.model.NotificationSentM;
import com.bbva.intranet.storage.domain.model.TopicM;
import com.bbva.intranet.storage.domain.model.UserM;
import com.bbva.intranet.storage.domain.model.UserTopicM;
import javassist.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
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
    private NotCoreUserTopicDAO notCoreUserTopicDAO;

    public NotCoreStorageImpl(Sender gnSender, Sender fcmSender, NotCoreDeviceDAO notCoreDeviceDAO, NotCoreNotificationDAO notCoreNotificationDAO, NotCoreNotificationSentDAO notCoreNotificationSentDAO, NotCoreTopicDAO notCoreTopicDAO, NotCoreUserDAO notCoreUserDAO, NotCoreUserTopicDAO notCoreUserTopicDAO) {
        this.gnSender = gnSender;
        this.fcmSender = fcmSender;
        this.notCoreDeviceDAO = notCoreDeviceDAO;
        this.notCoreNotificationDAO = notCoreNotificationDAO;
        this.notCoreNotificationSentDAO = notCoreNotificationSentDAO;
        this.notCoreTopicDAO = notCoreTopicDAO;
        this.notCoreUserDAO = notCoreUserDAO;
        this.notCoreUserTopicDAO = notCoreUserTopicDAO;
    }

    @Override
    public void register(NotCoreChannel channel, UserDeviceRegister deviceRegister) throws NotCoreException {
        NotCoreUtility.verifyChannel(channel);
        String email = deviceRegister.getUserId();
        String deviceId = deviceRegister.getUserId();
        UserM userM = null;
        DeviceM deviceM =  null;
        try {
            LOG.info("Fetching «%s» user", email);
            userM = notCoreUserDAO.findByEmail(email);
            LOG.info(String.format("«%s» is already exist", email));
        } catch (NoRecordFoundException e) {
            LOG.info(String.format("«%s» doesn't exist", email));
            userM = new UserM(email);
            try {
                LOG.info("User storage");
                notCoreUserDAO.save(userM);
            } catch (TransactionStoppedException saveUserException) {
                throw new NotCoreException(String.format("Failed user storage but it was registered in GNotifier"));
            }
        } catch (TransactionStoppedException e) {
            throw new NotCoreException(e.getMessage());
        }
        Long userId = userM.getId();
        try {
            LOG.info("Fetching device");
            deviceM = notCoreDeviceDAO.findBy(userId, deviceId);
            deviceM.setToken(deviceRegister.getToken());
            deviceM.setUpdateAt(new Date());
            try {
                LOG.info("Update device");
                notCoreDeviceDAO.update(deviceM);
            } catch (TransactionStoppedException e1) {
                throw new NotCoreException(String.format("Failed device UPDATE storage but it was registered in GNotifier"));
            }
        } catch (NoRecordFoundException e) {
            LOG.info(String.format("The device with id %s of user id %s was not found", deviceId, userId));
            deviceM = buildNewDeviceM(deviceRegister, userId);
            try {
                LOG.info("Storage device");
                notCoreDeviceDAO.save(deviceM);
            } catch (TransactionStoppedException e1) {
                throw new NotCoreException(String.format("Failed device storage but it was registered in GNotifier"));
            }
        } catch (TransactionStoppedException findDeviceError) {
            throw new NotCoreException(findDeviceError.getMessage());
        }
        try {
            LOG.info("Registry device in GNotifier");
            gnSender.register(deviceRegister);
        } catch (SenderException senderException) {
            try {
                if (deviceM.getUpdateAt() == null) {
                    LOG.error(senderException.getMessage());
                    LOG.info("Device will be delete because it are new and was failed in GNotifier register");
                    notCoreDeviceDAO.delete(deviceM);
                }
            } catch (TransactionStoppedException deleteException) {
                throw new NotCoreException(deleteException.getMessage());
            }
            throw new NotCoreException(senderException.getMessage());
        }
    }

    @Override
    public void sendNotification(NotCoreChannel channel, PushNotification pushNotification) throws NotCoreException {
        NotCoreUtility.verifyChannel(channel);
        NotificationM notificationM = null;
        UserM userM = null;
        TopicM topicM = null;
        Long templateId = pushNotification.getMessage().getTemplate().getId();
        String topicName = pushNotification.getTopic();
        String email = pushNotification.getUserId();
        NotificationSentM notificationSentM = new NotificationSentM();
        try {
            LOG.info(String.format("Fetching template with id %s", templateId));
            notificationM = this.notCoreNotificationDAO.findBy(templateId);
            notificationSentM.setNotificationId(notificationM.getId());
            try {
                LOG.info(String.format("Fetching user with email %s", email));
                userM = this.notCoreUserDAO.findByEmail(email);
                notificationSentM.setUserId(userM.getId());
            } catch (NoRecordFoundException userException) {
                LOG.info(String.format("User with email «%s» not found ", email));
            }
            try {
                LOG.info(String.format("Fetching topic with name %s", topicName));
                topicM = this.notCoreTopicDAO.findByName(topicName);
                if (userM != null) throw new NotCoreException("Can't send push notification, it must be specific by email or topic but not both");
                notificationSentM.setTopicId(topicM.getId());
            } catch (NoRecordFoundException topicException) {
                LOG.info(String.format("Topic with name «%s» not found ", topicName));
                if (userM == null) throw new NotCoreException("Can't send push notification cause user and topic are invalid.");
            }
        } catch (NoRecordFoundException e) {
            throw new NotCoreException(String.format("Notification with template id «%s» not found ", templateId));
        } catch (TransactionStoppedException e) {
            throw new NotCoreException(e.getMessage());
        }
        try {
            LOG.info("NotCore stores push notification sent");
            notificationSentM.setRead(false);
            notCoreNotificationSentDAO.save(notificationSentM);
            LOG.info("GNotifier send push notification");
            gnSender.sendNotification(pushNotification);
        } catch (TransactionStoppedException storageFailed) {
            throw new NotCoreException(String.format("The notification couldn't stored"));
        } catch (SenderException senderException) {
            try {
                notCoreNotificationSentDAO.delete(notificationSentM);
            } catch (TransactionStoppedException deleteException) {
                throw new NotCoreException(deleteException.getMessage());
            }
            throw new NotCoreException(senderException.getMessage());
        }
    }

    @Override
    public void multiSendNotification(NotCoreChannel channel, Set<PushNotification> notifications) throws NotCoreException {
        NotCoreUtility.verifyChannel(channel);
        int errorsCount = 0;
        for (PushNotification pushNotification : notifications) {
            try {
                sendNotification(channel, pushNotification);
            } catch (NotCoreException e) {
                errorsCount++;
                LOG.info(String.format("Notification error number: %s | message error: %s", errorsCount, e.getMessage()));
            }
        }
        if (errorsCount == notifications.size()) throw new NotCoreException("Couldn't sent any notification");
    }

    @Override
    public void createTopic(NotCoreChannel channel, Topic topic) throws NotCoreException {
        NotCoreUtility.verifyChannel(channel);
        try {
            notCoreTopicDAO.findByName(topic.getName());
            throw new NotCoreException(String.format("%s topic is already exist", topic.getName()));
        } catch (NoRecordFoundException e) {
            TopicM topicM = new TopicM();
                topicM.setName(topic.getName());
                topicM.setDescription(topic.getDescription());
            try {
                LOG.info("Topic storage");
                notCoreTopicDAO.save(topicM);
                LOG.info("GNotifier create topic");
                this.gnSender.createTopic(topic);
            } catch (TransactionStoppedException storageFailed) {
                throw new NotCoreException(storageFailed.getMessage());
            } catch (SenderException senderException) {
                try {
                    LOG.info("Delete topic that failed in GN");
                    notCoreTopicDAO.delete(topicM);
                } catch (TransactionStoppedException deleteException) {
                    throw new NotCoreException(deleteException.getMessage());
                }
                throw new NotCoreException(senderException.getMessage());
            }
        } catch (TransactionStoppedException e) {
            throw new NotCoreException(e.getMessage());
        }
    }

    @Override
    public void deleteTopic(NotCoreChannel channel, String topicName) throws NotCoreException {
        NotCoreUtility.verifyChannel(channel);
        try {
            LOG.info(String.format("Fetching topic with %s name", topicName));
            TopicM topicM = notCoreTopicDAO.findByName(topicName);
            LOG.info("GNotifier delete topic");
            gnSender.deleteTopic(topicName);
            LOG.info("NotCore delete topic");
            notCoreTopicDAO.delete(topicM);
        } catch (SenderException | NoRecordFoundException e) {
            throw new NotCoreException(e.getMessage());
        } catch (TransactionStoppedException e) {
            throw new NotCoreException(e.getMessage());
        }
    }

    @Override
    public void updateTopic(NotCoreChannel channel, String topicName, Topic topic) throws NotCoreException {
        NotCoreUtility.verifyChannel(channel);
        try {
            LOG.info(String.format("Fetching topic with name %s", topicName));
            TopicM topicM = notCoreTopicDAO.findByName(topicName);
                topicM.setName(topic.getName());
                topicM.setDescription(topic.getDescription());
            LOG.info(String.format("NotCore update topic name %s", topicName));
            notCoreTopicDAO.update(topicM);
            LOG.info(String.format("GNotifier update topic name %s", topicName));
            gnSender.updateTopic(topicName, topic);
        } catch (NoRecordFoundException e) {
            throw new NotCoreException(String.format("«%s» topic doesn't exist", topicName));
        } catch (TransactionStoppedException e) {
            throw new NotCoreException(e.getMessage());
        } catch (SenderException e) {
            throw new NotCoreException(e.getMessage());
        }
    }

    @Override
    public void subscribeUserIntoTopic(NotCoreChannel channel, String topicName, UserToSubscribe userToSubscribe) throws NotCoreException {
        NotCoreUtility.verifyChannel(channel);
        Long userId = null;
        Long topicId = null;
        String email = userToSubscribe.getUser();
        try {
            LOG.info(String.format("Fetching topic with name %s", topicName));
            TopicM topicM = notCoreTopicDAO.findByName(topicName);
            topicId = topicM.getId();
            try {
                LOG.info(String.format("Fetching user with email %s", email));
                UserM userM = notCoreUserDAO.findByEmail(email);
                userId = userM.getId();
            } catch (NoRecordFoundException e) {
                throw new NotCoreException(String.format("«%s» doesn't exist", email));
            }
        } catch (NoRecordFoundException e) {
            throw new NotCoreException(String.format("«%s» topic doesn't exist", topicName));
        } catch (TransactionStoppedException e) {
            throw new NotCoreException(e.getMessage());
        }
        try {
            LOG.info(String.format("Checking if «%s» is already in «%s» topic", email, topicName));
            notCoreUserTopicDAO.findBy(userId, topicId);
            throw new NotCoreException(String.format("«%s» is already exist in «%s»", email, topicName));
        } catch (NotFoundException e) {
            UserTopicM userTopicM = new UserTopicM();
                userTopicM.setTopicId(topicId);
                userTopicM.setUserId(userId);
            try {
                LOG.info(String.format("Storage «%s» in «%s» topic", email, topicName));
                notCoreUserTopicDAO.save(userTopicM);
                LOG.info(String.format("GNotifier subscribe «%s» in «%s» topic", email, topicName));
                gnSender.subscribeUserIntoTopic(topicName, userToSubscribe);
            } catch (TransactionStoppedException e1) {
                throw new NotCoreException(e.getMessage());
            } catch (SenderException senderException) {
                try {
                    notCoreUserTopicDAO.delete(userTopicM);
                    throw new NotCoreException(senderException.getMessage());
                } catch (TransactionStoppedException deleteException) {
                    LOG.error(senderException.getMessage());
                    throw new NotCoreException(deleteException.getMessage());
                }
            }
        } catch (TransactionStoppedException e) {
            throw new NotCoreException(e.getMessage());
        }
    }

    @Override
    public void unSubscribeUserIntoTopic(NotCoreChannel channel, String topicName, UserToUnSubscribe userToUnSubscribe) throws NotCoreException {
        NotCoreUtility.verifyChannel(channel);
        Long userId = null;
        Long topicId = null;
        String email = userToUnSubscribe.getUserId();
        TopicM topicM = null;
        try {
            LOG.info(String.format("Fetching topic with name %s", topicName));
            topicId = notCoreTopicDAO.findByName(topicName).getId();
            try {
                LOG.info(String.format("Fetching user with email %s", email));
                userId = notCoreUserDAO.findByEmail(email).getId();
            } catch (NoRecordFoundException e) {
                throw new NotCoreException(String.format("«%s» doesn't exist", email));
            }
        } catch (NoRecordFoundException e) {
            throw new NotCoreException(String.format("«%s» topic doesn't exist", topicName));
        } catch (TransactionStoppedException e) {
            throw new NotCoreException(e.getMessage());
        }
        try {
            LOG.info(String.format("Checking if «%s» is already in «%s» topic", email, topicName));
            UserTopicM userTopicM = notCoreUserTopicDAO.findBy(userId, topicId);
            LOG.info(String.format("Remove «%s» from «%s» topic", email, topicName));
            notCoreUserTopicDAO.delete(userTopicM);
            LOG.info(String.format("GNotifier remove «%s» from «%s» topic", email, topicName));
            gnSender.unSubscribeUserIntoTopic(topicName, userToUnSubscribe);
        } catch (NotFoundException e) {
            throw new NotCoreException(String.format("«%s» doesn't exist in «%s»", email, topicName));
        } catch (TransactionStoppedException e) {
            throw new NotCoreException(e.getMessage());
        } catch (SenderException senderException) {
            try {
                UserTopicM userTopicM = new UserTopicM();
                    userTopicM.setTopicId(topicId);
                    userTopicM.setUserId(userId);
                notCoreUserTopicDAO.save(userTopicM);
                throw new NotCoreException(senderException.getMessage());
            } catch (TransactionStoppedException saveException) {
                LOG.error(senderException.getMessage());
                throw new NotCoreException(saveException.getMessage());
            }
        }
    }

    private static DeviceM buildNewDeviceM(UserDeviceRegister deviceRegister, Long userId) {
        DeviceM deviceM = new DeviceM();
        deviceM.setUserId(userId);
        deviceM.setBrand(deviceRegister.getDevice().getBrand());
        deviceM.setDeviceId(deviceRegister.getDevice().getId());
        deviceM.setLanguage(deviceRegister.getDevice().getLanguage());
        deviceM.setModel(deviceRegister.getDevice().getModel());
        deviceM.setOs(deviceRegister.getDevice().getOs());
        deviceM.setOsVersion(deviceRegister.getDevice().getOsVersion());
        deviceM.setToken(deviceRegister.getToken());
        deviceM.setCreateAt(new Date());
        deviceM.setUpdateAt(null);
        return deviceM;
    }
}
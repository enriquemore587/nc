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
import com.bbva.intranet.senders.domain.requests.desregister.Desregister;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

import static com.bbva.intranet.not.core.utilities.NotCoreUtility.STARTING;
import static com.bbva.intranet.not.core.utilities.NotCoreUtility.FINISHED;

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
    public void register(NotCoreChannel channel, UserDeviceRegister deviceRegister) throws NotCoreException, SenderException {
        LOG.info(STARTING);
        NotCoreUtility.verifyChannel(channel);
        NotCoreUtility.verifyPojo(deviceRegister);
        LOG.info("Registry device in GNotifier");
        gnSender.register(deviceRegister);
        deviceStorage(deviceRegister);
        LOG.info(FINISHED);
    }

    private void deviceStorage(UserDeviceRegister deviceRegister) throws NotCoreException {
        LOG.info(STARTING);
        UserM userM = fetchUserM(deviceRegister);
        saveUpdateDeviceM(deviceRegister, userM);
        LOG.info(FINISHED);
    }

    private UserM fetchUserM(UserDeviceRegister deviceRegister) throws NotCoreException {
        LOG.info(STARTING);
        String email = deviceRegister.getUserId();
        UserM userM = null;
        try {
            LOG.info("Fetching user with email «%s»", email);
            userM = notCoreUserDAO.findByEmail(email);
            LOG.info(String.format("«%s» is already exist", email));
        } catch (NoRecordFoundException e) {
            LOG.info(String.format("«%s» doesn't exist", email));
            userM = new UserM(email);
            try {
                LOG.info("User storage. Cause doesn't exist");
                notCoreUserDAO.save(userM);
            } catch (TransactionStoppedException saveUserException) {
                LOG.error(saveUserException.getMessage());
                throw new NotCoreException(String.format("Failed user storage"));
            }
        } catch (TransactionStoppedException e) {
            throw new NotCoreException(e.getMessage());
        }
        LOG.info(FINISHED);
        return userM;
    }

    private void saveUpdateDeviceM(UserDeviceRegister deviceRegister, UserM userM) throws NotCoreException {
        LOG.info(STARTING);
        NotCoreUtility.verifyPojo(userM);
        String deviceId = deviceRegister.getDevice().getId();
        DeviceM deviceM = null;
        try {
            LOG.info(String.format("Fetching device of user with email: «%s» and Id: «%s»", userM.getEmail(), userM.getId()));
            deviceM = notCoreDeviceDAO.findBy(userM.getId(), deviceId);
            deviceM.setToken(deviceRegister.getToken());
            deviceM.setUpdateAt(new Date());
            LOG.info(String.format("Device with email: «%s» and Id: «%s» is already exist", userM.getEmail(), userM.getId()));
            try {
                LOG.info("Update device");
                notCoreDeviceDAO.update(deviceM);
            } catch (TransactionStoppedException e1) {
                throw new NotCoreException(String.format("Failed device UPDATE storage but it was registered in GNotifier"));
            }
        } catch (NoRecordFoundException e) {
            LOG.info(String.format("The device with id %s of user id %s was not found. Will be created", deviceId, userM.getId()));
            deviceM = buildNewDeviceM(deviceRegister, userM.getId());
            try {
                LOG.info("Storage device");
                notCoreDeviceDAO.save(deviceM);
            } catch (TransactionStoppedException e1) {
                throw new NotCoreException(String.format("Failed device storage but it was registered in GNotifier"));
            }
        } catch (TransactionStoppedException findDeviceError) {
            throw new NotCoreException(findDeviceError.getMessage());
        }
        LOG.info(FINISHED);
    }

    @Override
    public void desRegister(NotCoreChannel channel, Desregister desregister) throws NotCoreException, SenderException {

    }

    @Override
    public void sendNotification(NotCoreChannel channel, PushNotification pushNotification) throws NotCoreException, SenderException {
        LOG.info(STARTING);
        NotCoreUtility.verifyChannel(channel);
        NotCoreUtility.verifyPojo(pushNotification);
        LOG.info("GNotifier send push notification");
        gnSender.sendNotification(pushNotification);
        saveNotificationSentM(findNotificationSentM(pushNotification));
        LOG.info(FINISHED);
    }

    private NotificationSentM findNotificationSentM(PushNotification pushNotification) throws NotCoreException {
        LOG.info(STARTING);
        NotificationSentM notificationSentM = new NotificationSentM();
        try {
            LOG.info(String.format("Fetching template with id %s", pushNotification.getMessage().getTemplate().getId()));
            NotificationM notificationM = notCoreNotificationDAO.findBy(pushNotification.getMessage().getTemplate().getId());
            LOG.info("Template found");
            notificationSentM.setNotificationId(notificationM.getId());
            try {
                String email = pushNotification.getUserId() == null ? "email is null" : pushNotification.getUserId();
                LOG.info(String.format("Fetching user with email %s", email));
                UserM userM = notCoreUserDAO.findByEmail(email);
                LOG.info(String.format("UserM found. The notification must was send by «%s»", email));
                notificationSentM.setUserId(userM.getId());
            } catch (NoRecordFoundException userException) {
                LOG.info("User with email not found. The notification must was send by topic");
                try {
                    String topicName = pushNotification.getTopic() == null ? "Topic name is null" : pushNotification.getTopic();
                    LOG.info(String.format("Fetching topic with name «%s»", topicName));
                    notificationSentM.setTopicId(this.notCoreTopicDAO.findByName(topicName).getId());
                    LOG.info("TopicM found");
                } catch (NoRecordFoundException topicException) {
                    throw new NotCoreException("Not notification type found. Tt's must be specific by topic or my email");
                }
            }
        } catch (NoRecordFoundException e) {
            throw new NotCoreException(String.format("Notification with template id «%s» not found ", pushNotification.getMessage().getTemplate().getId()));
        } catch (TransactionStoppedException e) {
            throw new NotCoreException(e.getMessage());
        }
        LOG.info(FINISHED);
        return notificationSentM;
    }

    private void saveNotificationSentM(NotificationSentM notificationSentM) throws NotCoreException {
        LOG.info(STARTING);
        try {
            LOG.info("NotCore stores push notification sent");
            notificationSentM.setRead(false);
            notCoreNotificationSentDAO.save(notificationSentM);
        } catch (TransactionStoppedException storageFailed) {
            throw new NotCoreException(String.format("The notification couldn't stored"));
        }
        LOG.info(FINISHED);
    }

    @Override
    public void createTopic(NotCoreChannel channel, Topic topic) throws NotCoreException, SenderException {
        LOG.info(STARTING);
        NotCoreUtility.verifyChannel(channel);
        NotCoreUtility.verifyPojo(topic);
        gnSender.createTopic(topic);
        try {
            LOG.info("Verifying that doesn't exist topic");
            notCoreTopicDAO.findByName(topic.getName());
            throw new NotCoreException(String.format("«%s» topic is already exist", topic.getName()));
        } catch (NoRecordFoundException e) {
            try {
                LOG.info("Topic is new");
                notCoreTopicDAO.save(new TopicM(topic.getName(), topic.getDescription()));
            } catch (TransactionStoppedException storageFailed) {
                throw new NotCoreException(storageFailed.getMessage());
            }
        } catch (TransactionStoppedException e) {
            throw new NotCoreException(e.getMessage());
        }
        LOG.info(FINISHED);
    }

    @Override
    public void deleteTopic(NotCoreChannel channel, String topicName) throws NotCoreException, SenderException {
        LOG.info(STARTING);
        NotCoreUtility.verifyChannel(channel);
        LOG.info("GNotifier delete topic");
        gnSender.deleteTopic(topicName);
        try {
            LOG.info(String.format("Fetching topic with %s name", topicName));
            TopicM topicM = notCoreTopicDAO.findByName(topicName);
            LOG.info("NotCore delete topic");
            notCoreTopicDAO.delete(topicM);
        } catch (NoRecordFoundException e) {
            throw new NotCoreException(String.format("«%s» topic doesn't exist", topicName));
        } catch (TransactionStoppedException e) {
            throw new NotCoreException(e.getMessage());
        }
        LOG.info(FINISHED);
    }

    @Override
    public void updateTopic(NotCoreChannel channel, String topicName, Topic topic) throws NotCoreException, SenderException {
        LOG.info(STARTING);
        NotCoreUtility.verifyChannel(channel);
        LOG.info("GNotifier update");
        gnSender.updateTopic(topicName, topic);
        try {
            LOG.info(String.format("Fetching topic with name %s", topicName));
            TopicM topicM = notCoreTopicDAO.findByName(topicName);
                topicM.setName(topic.getName());
                topicM.setDescription(topic.getDescription());
            LOG.info("NotCore update topic");
            notCoreTopicDAO.update(topicM);
        } catch (NoRecordFoundException e) {
            throw new NotCoreException(String.format("«%s» topic doesn't exist", topicName));
        } catch (TransactionStoppedException e) {
            throw new NotCoreException(e.getMessage());
        }
        LOG.info(FINISHED);
    }

    @Override
    public void subscribeUserIntoTopic(NotCoreChannel channel, String topicName, UserToSubscribe userToSubscribe) throws NotCoreException, SenderException {
        LOG.info(STARTING);
        NotCoreUtility.verifyChannel(channel);
        String email = userToSubscribe.getUser();
        LOG.info(String.format("GNotifier subscribe «%s» in «%s» topic", email, topicName));
        gnSender.subscribeUserIntoTopic(topicName, userToSubscribe);
        UserTopicM userTopicM = findUserTopicMBy(topicName, email);
        try {
            LOG.info(String.format("Checking if «%s» is already in «%s» topic", email, topicName));
            notCoreUserTopicDAO.findBy(userTopicM.getUserId(), userTopicM.getTopicId());
            throw new NotCoreException(String.format("«%s» is already exist in «%s»", email, topicName));
        } catch (NoRecordFoundException e) {
            try {
                LOG.info(String.format("Storage «%s» in «%s» topic", email, topicName));
                notCoreUserTopicDAO.save(userTopicM);
            } catch (TransactionStoppedException e1) {
                throw new NotCoreException(e.getMessage());
            }
        } catch (TransactionStoppedException e) {
            throw new NotCoreException(e.getMessage());
        }
        LOG.info(FINISHED);
    }

    private UserTopicM findUserTopicMBy(String topicName, String email) throws NotCoreException {
        LOG.info(STARTING);
        UserTopicM userTopicM = new UserTopicM();
        try {
            LOG.info(String.format("Fetching topic with name %s", topicName));
            userTopicM.setTopicId(notCoreTopicDAO.findByName(topicName).getId());
            try {
                LOG.info(String.format("Fetching user with email %s", email));
                userTopicM.setUserId(notCoreUserDAO.findByEmail(email).getId());
            } catch (NoRecordFoundException e) {
                throw new NotCoreException(String.format("«%s» doesn't exist", email));
            }
        } catch (NoRecordFoundException e) {
            throw new NotCoreException(String.format("«%s» topic doesn't exist", topicName));
        } catch (TransactionStoppedException e) {
            throw new NotCoreException(e.getMessage());
        }
        LOG.info(FINISHED);
        return userTopicM;
    }

    @Override
    public void unSubscribeUserIntoTopic(NotCoreChannel channel, String topicName, UserToUnSubscribe userToUnSubscribe) throws NotCoreException, SenderException {
        LOG.info(STARTING);
        NotCoreUtility.verifyChannel(channel);
        String email = userToUnSubscribe.getUserId();
        LOG.info(String.format("GNotifier remove «%s» from «%s» topic", email, topicName));
        gnSender.unSubscribeUserIntoTopic(topicName, userToUnSubscribe);
        try {
            LOG.info("Verifying that topics and user already exist into database");
            UserTopicM userTopicM = findUserTopicMBy(topicName, email);
            LOG.info(String.format("Checking if «%s» is already in «%s» topic", email, topicName));
            userTopicM = notCoreUserTopicDAO.findBy(userTopicM.getUserId(), userTopicM.getTopicId());
            LOG.info(String.format("Remove «%s» from «%s» topic", email, topicName));
            notCoreUserTopicDAO.delete(userTopicM);
        } catch (NoRecordFoundException e) {
            throw new NotCoreException(String.format("«%s» doesn't exist in «%s»", email, topicName));
        } catch (TransactionStoppedException e) {
            throw new NotCoreException(e.getMessage());
        }
        LOG.info(FINISHED);
    }

    private static DeviceM buildNewDeviceM(UserDeviceRegister deviceRegister, Long userId) {
        LOG.info(STARTING);
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
        LOG.info(FINISHED);
        return deviceM;
    }
}
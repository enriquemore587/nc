package com.bbva.intranet.not.core.dao.impl;

import com.bbva.intranet.not.core.dao.NotCore;
import com.bbva.intranet.not.core.exceptions.NotCoreException;
import com.bbva.intranet.not.core.utilities.NotCoreChannel;
import com.bbva.intranet.senders.domain.requests.UserToSubscribe;
import com.bbva.intranet.senders.domain.requests.UserToUnSubscribe;
import com.bbva.intranet.senders.domain.requests.notifications.PushNotification;
import com.bbva.intranet.senders.domain.requests.register.UserDeviceRegister;
import com.bbva.intranet.senders.domain.requests.topics.Topic;
import com.bbva.intranet.storage.domain.dao.StorageManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

public class NotCoreStorageImpl implements NotCore {

    public static Logger LOG = LoggerFactory.getLogger(NotCoreStorageImpl.class);
    private StorageManager storageManager;

    public NotCoreStorageImpl(StorageManager storageManager) {
        this.storageManager = storageManager;
    }

    @Override
    public void register(NotCoreChannel channel, UserDeviceRegister deviceRegister) throws NotCoreException {

    }

    @Override
    public void sendNotification(NotCoreChannel channel, PushNotification notification) throws NotCoreException {

    }

    @Override
    public void multiSendNotification(NotCoreChannel channel, Set<PushNotification> notifications) throws NotCoreException {

    }

    @Override
    public void createTopic(NotCoreChannel channel, Topic topic) throws NotCoreException {

    }

    @Override
    public void deleteTopic(NotCoreChannel channel, String topicName) throws NotCoreException {

    }

    @Override
    public void updateTopic(NotCoreChannel channel, String topicName, Topic topic) throws NotCoreException {

    }

    @Override
    public void subscribeUserIntoTopic(NotCoreChannel channel, String topicName, UserToSubscribe userToSubscribe) throws NotCoreException {

    }

    @Override
    public void unSubscribeUserIntoTopic(NotCoreChannel channel, String topicName, UserToUnSubscribe userToUnSubscribe) throws NotCoreException {

    }
}
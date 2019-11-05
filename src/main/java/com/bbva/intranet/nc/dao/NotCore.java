package com.bbva.intranet.nc.dao;

import com.bbva.intranet.nc.exceptions.NotCoreException;
import com.bbva.intranet.nc.utilities.NotCoreChannel;
import com.bbva.intranet.senders.domain.requests.UserToSubscribe;
import com.bbva.intranet.senders.domain.requests.UserToUnSubscribe;
import com.bbva.intranet.senders.domain.requests.notifications.PushNotification;
import com.bbva.intranet.senders.domain.requests.register.UserDeviceRegister;
import com.bbva.intranet.senders.domain.requests.topics.Topic;
import com.bbva.intranet.senders.domain.responses.TopicResp;

import java.util.Set;

public interface NotCore {

    void register(NotCoreChannel channel, UserDeviceRegister deviceRegister) throws NotCoreException;
    void sendNotification(NotCoreChannel channel, PushNotification notification) throws NotCoreException;
    void multiSendNotification(NotCoreChannel channel, Set<PushNotification> notifications) throws NotCoreException;

    TopicResp topicsBySenderEmail(NotCoreChannel channel, Integer pageSize, Integer paginationKey) throws NotCoreException;

    void createTopic(NotCoreChannel channel, Topic topic) throws NotCoreException;
    void deleteTopic(NotCoreChannel channel, String topicName) throws NotCoreException;
    void updateTopic(NotCoreChannel channel, String topicName, Topic topic) throws NotCoreException;
    void subscribeUserIntoTopic(NotCoreChannel channel, String topicName, UserToSubscribe userToSubscribe) throws NotCoreException;
    void unSubscribeUserIntoTopic(NotCoreChannel channel, String topicName, UserToUnSubscribe userToUnSubscribe) throws NotCoreException;

}

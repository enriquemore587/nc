package com.bbva.intranet.senders.domain.dao;

import com.bbva.intranet.senders.domain.requests.UserToSubscribe;
import com.bbva.intranet.senders.domain.requests.UserToUnSubscribe;
import com.bbva.intranet.senders.domain.requests.notifications.PushNotification;
import com.bbva.intranet.senders.domain.requests.register.UserDeviceRegister;
import com.bbva.intranet.senders.domain.requests.topics.Topic;
import com.bbva.intranet.senders.domain.responses.TopicResp;
import com.bbva.intranet.senders.exceptions.SenderException;

import java.util.Set;

public interface Sender {

    void register(UserDeviceRegister userDeviceRegister) throws SenderException;

    void send(PushNotification pushNotification) throws SenderException;
    void multiSend(Set<PushNotification> notifications) throws SenderException;

    TopicResp topicsBySenderEmail(Integer pageSize, Integer paginationKey) throws SenderException;

    void createTopic(Topic topic) throws SenderException;
    void deleteTopic(String topicName) throws SenderException;
    void updateTopic(String topicName, Topic topic) throws SenderException;
    void subscribeUserIntoTopic(String topicName, UserToSubscribe userToSubscribe) throws SenderException;
    void unSubscribeUserIntoTopic(String topicName, UserToUnSubscribe userToUnSubscribe) throws SenderException;

}

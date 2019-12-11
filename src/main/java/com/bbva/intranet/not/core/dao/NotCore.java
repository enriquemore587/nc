package com.bbva.intranet.not.core.dao;

import com.bbva.intranet.not.core.exceptions.NotCoreException;
import com.bbva.intranet.not.core.utilities.NotCoreChannel;
import com.bbva.intranet.senders.domain.requests.UserToSubscribe;
import com.bbva.intranet.senders.domain.requests.UserToUnSubscribe;
import com.bbva.intranet.senders.domain.requests.desregister.Desregister;
import com.bbva.intranet.senders.domain.requests.notifications.PushNotification;
import com.bbva.intranet.senders.domain.requests.register.UserDeviceRegister;
import com.bbva.intranet.senders.domain.requests.topics.Topic;
import com.bbva.intranet.senders.exceptions.SenderException;

public interface NotCore {

    void register(NotCoreChannel channel, UserDeviceRegister deviceRegister) throws NotCoreException, SenderException;

    void desRegister(NotCoreChannel channel, Desregister desregister) throws NotCoreException, SenderException;
    
    void sendNotification(NotCoreChannel channel, PushNotification pushNotification) throws NotCoreException, SenderException;
    
    void createTopic(NotCoreChannel channel, Topic topic) throws NotCoreException, SenderException;
    
    void deleteTopic(NotCoreChannel channel, String topicName) throws NotCoreException, SenderException;
    
    void updateTopic(NotCoreChannel channel, String topicName, Topic topic) throws NotCoreException, SenderException;
    
    void subscribeUserIntoTopic(NotCoreChannel channel, String topicName, UserToSubscribe userToSubscribe) throws NotCoreException, SenderException;
    
    void unSubscribeUserIntoTopic(NotCoreChannel channel, String topicName, UserToUnSubscribe userToUnSubscribe) throws NotCoreException, SenderException;
    
}

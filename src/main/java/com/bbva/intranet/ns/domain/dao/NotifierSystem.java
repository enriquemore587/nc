package com.bbva.intranet.ns.domain.dao;

import com.bbva.intranet.data.exceptions.GAEAuthorizationException;
import com.bbva.intranet.ns.exceptions.NotifierSystemException;
import com.bbva.intranet.ns.utilities.NSChannel;
import com.bbva.intranet.senders.domain.requests.devices.Device;
import com.bbva.intranet.senders.domain.requests.notifications.PushNotification;

public interface NotifierSystem {

    void register(String channel, Device device);
    void sendNotification(NSChannel channel, PushNotification notification) throws NotifierSystemException;

}

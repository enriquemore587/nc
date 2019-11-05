package com.bbva.intranet.ns.domain.dao.impl;

import com.bbva.intranet.data.exceptions.GAEAuthorizationException;
import com.bbva.intranet.ns.domain.dao.NotifierSystem;
import com.bbva.intranet.ns.exceptions.NotifierSystemException;
import com.bbva.intranet.ns.utilities.NSChannel;
import com.bbva.intranet.ns.utilities.NSUtility;
import com.bbva.intranet.senders.domain.dao.Sender;
import com.bbva.intranet.senders.domain.requests.devices.Device;
import com.bbva.intranet.senders.domain.requests.notifications.PushNotification;
import com.bbva.intranet.senders.exceptions.SenderException;
import com.bbva.intranet.utilities.GsonGAEUtility;
import com.fga.exceptions.OAuthClientException;
import com.fga.oauth.client.GatewayClient;
import com.fga.oauth.client.utils.OAuthResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.nio.charset.Charset;

@Component
@Qualifier("lazy")
public class NSLazyImpl implements NotifierSystem {

    public static Logger LOG = LoggerFactory.getLogger(NSLazyImpl.class);

    private Sender gnSender;

    private Sender fcmSender;

    @Autowired
    public NSLazyImpl(@Qualifier("gn") Sender gnSender, @Qualifier("fcm") Sender fcmSender) {
        this.gnSender = gnSender;
        this.fcmSender = fcmSender;
    }

    @Override
    public void register(String channel, Device device) {

    }

    @Override
    public void sendNotification(NSChannel channel, PushNotification notification) throws NotifierSystemException {
        try {
            if (channel == NSChannel.GNOTIFIER) {
                gnSender.send(notification);
            } else if (channel == NSChannel.FCM){
                LOG.info(channel.value);
            }
        } catch (SenderException e) {
            LOG.error(channel.value);
            throw new NotifierSystemException(e.getMessage());
        }
    }
}

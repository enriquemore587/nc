package com.bbva.intranet.ns.domain.dao.impl;

import com.bbva.intranet.ns.domain.dao.NotifierSystem;
import com.bbva.intranet.ns.exceptions.NotifierSystemException;
import com.bbva.intranet.ns.utilities.NSChannel;
import com.bbva.intranet.senders.domain.requests.devices.Device;
import com.bbva.intranet.senders.domain.requests.notifications.PushNotification;
import com.bbva.intranet.storage.domain.dao.StorageManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@Qualifier("eager")
public class NSEagerImpl implements NotifierSystem {

    public static Logger LOG = LoggerFactory.getLogger(NSEagerImpl.class);
    private StorageManager storageManager;

    @Autowired
    public NSEagerImpl(StorageManager storageManager) {
        this.storageManager = storageManager;
    }

    @Override
    public void register(String channel, Device device) {

    }

    @Override
    public void sendNotification(NSChannel channel, PushNotification notification) throws NotifierSystemException {
        LOG.info("eager");
    }
}

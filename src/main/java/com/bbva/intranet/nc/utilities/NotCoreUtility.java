package com.bbva.intranet.nc.utilities;

import com.bbva.intranet.nc.exceptions.NotCoreException;
import com.bbva.intranet.senders.domain.requests.register.ApplicationToUserToRegister;
import com.fga.utils.PropertiesUtil;

public abstract class NotCoreUtility {

    // TODO: This attribute will be used in version 3
    public final static String NC_APP_KEY = PropertiesUtil.getString("ns.appKey");
    public final static String NC_SENDER_EMAIL = PropertiesUtil.getString("ns.sender.email");
    public final static String NC_SENDER_ID = PropertiesUtil.getString("ns.sender.id");
    public final static String NC_APPLICATION_ID = PropertiesUtil.getString("ns.id.application");
    public final static String NC_NAME = PropertiesUtil.getString("ns.name");
    public final static String NC_VERSION = PropertiesUtil.getString("ns.version");
    public final static String STARTING = "Starting";
    public final static String FINISHED = "Finished";

    // TODO: This method will be removed to next versions when FMC is enable.
    public static void verifyChannel(NotCoreChannel channel) throws NotCoreException {
        if (channel != NotCoreChannel.GNOTIFIER) {
            throw new NotCoreException("This version is only to: " + NotCoreChannel.GNOTIFIER);
        }
    }

    public static ApplicationToUserToRegister buildApplicationToUserToRegister() {
        ApplicationToUserToRegister application = new ApplicationToUserToRegister();
        application.setId(Long.parseLong(NotCoreUtility.NC_APPLICATION_ID));
        application.setLanguage("en");
        application.setName(NotCoreUtility.NC_NAME);
        application.setVersion(NotCoreUtility.NC_VERSION);
        return application;
    }
}

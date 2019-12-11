package com.bbva.intranet.not.core.utilities;

import com.bbva.intranet.senders.domain.requests.desregister.ApplicationDesregister;
import com.bbva.intranet.senders.domain.requests.desregister.Desregister;
import com.bbva.intranet.senders.domain.requests.desregister.DeviceDesregister;
import com.bbva.intranet.senders.domain.requests.notifications.ApplicationPN;
import com.bbva.intranet.senders.domain.requests.register.ApplicationToUserToRegister;
import com.bbva.intranet.not.core.exceptions.NotCoreException;
import com.fga.utils.PropertiesUtil;
import org.hibernate.Session;
import org.hibernate.jdbc.ReturningWork;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public abstract class NotCoreUtility {

    // TODO: This attribute will be used in version 3
    public final static String NC_APP_KEY = PropertiesUtil.getString("ns.appKey");
    public final static String NC_SENDER_EMAIL = PropertiesUtil.getString("ns.sender.email");
    public final static String NC_SENDER_ID = PropertiesUtil.getString("ns.sender.id");
    public final static Long NC_APPLICATION_ID = Long.parseLong(PropertiesUtil.getString("ns.id.application"));
    public final static String NC_NAME = PropertiesUtil.getString("ns.name");
    public final static String NC_VERSION = PropertiesUtil.getString("ns.version");
    public final static Long NC_GENERIC_TEMPLATE = Long.parseLong(PropertiesUtil.getString("ns.generic.template"));

    public final static String STARTING = "Starting";
    public final static String FINISHED = "Finished";
    public static final String RECORD_NO_FOUND_MESSAGE = "No record found. ";
    public static final String TRANSACTION_STOPPED_MESSAGE = "Transaction stopped. ";
    public static final String UNCONTROLLED_ERROR_MESSAGE = "Uncontrolled error. ";

    // TODO: This method will be removed to next versions when FMC is enable.
    public static void verifyChannel(NotCoreChannel channel) throws NotCoreException {
        if (channel != NotCoreChannel.GNOTIFIER) throw new NotCoreException("This version is only to: " + NotCoreChannel.GNOTIFIER);
    }

    public static ApplicationToUserToRegister buildApplicationToUserToRegister() {
        ApplicationToUserToRegister application = new ApplicationToUserToRegister();
        application.setId(NotCoreUtility.NC_APPLICATION_ID);
        application.setLanguage("en");
        application.setName(NotCoreUtility.NC_NAME);
        application.setVersion(NotCoreUtility.NC_VERSION);
        return application;
    }

    public static ApplicationPN buildApplicationPN() {
        ApplicationPN applicationPN = new ApplicationPN(NC_APPLICATION_ID);
        return applicationPN;
    }


    public static Desregister buildDesregister(String email, String uniqueDeviceId) {
        Desregister desregister = new Desregister();
        desregister.setApplication(new ApplicationDesregister(NC_APPLICATION_ID));
        desregister.setDevice(new DeviceDesregister(uniqueDeviceId));
        desregister.setUserId(email);
        return desregister;
    }

    public static void enableEmojis(Session session) {
        session.doReturningWork(new ReturningWork<Object>() {
            @Override
            public Object execute(Connection connection) throws SQLException {
                try(Statement stmt = connection.createStatement()) {
                    stmt.executeQuery("SET NAMES utf8mb4");
                }
                return null;
            }
        });
    }

    public static void verifyPojo(Object pojo) throws NotCoreException {
        if (pojo == null) throw new NotCoreException(String.format("«%s» is null", pojo.getClass().getSimpleName()));
    }

    public static void verifyObject(Object attribute, String name) throws NotCoreException {
        if (attribute == null) throw new NotCoreException(String.format("«%s» is null", name));
    }

}

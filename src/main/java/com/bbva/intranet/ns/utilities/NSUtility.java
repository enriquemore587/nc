package com.bbva.intranet.ns.utilities;

import com.fga.utils.PropertiesUtil;

public abstract class NSUtility {
    public final static String NS_APP_KEY = PropertiesUtil.getString("ns.appKey");
    public final static String NS_SENDER_EMAIL = PropertiesUtil.getString("ns.sender.email");
}

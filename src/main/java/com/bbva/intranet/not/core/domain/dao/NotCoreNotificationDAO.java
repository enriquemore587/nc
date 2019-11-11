package com.bbva.intranet.not.core.domain.dao;

import com.bbva.intranet.not.core.domain.model.NotificationM;

import java.util.List;

public interface NotCoreNotificationDAO {

    List<NotificationM> findAll();
    NotificationM findById(Long id);
    void save(NotificationM notificationM);
    void update(NotificationM notificationM);

}

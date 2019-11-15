package com.bbva.intranet.storage.domain.dao;

import com.bbva.intranet.storage.domain.model.NotificationM;
import com.bbva.intranet.not.core.exceptions.NoRecordFoundException;
import com.bbva.intranet.not.core.exceptions.TransactionStoppedException;

import java.util.List;

public interface NotCoreNotificationDAO {

    List<NotificationM> findAll() throws NoRecordFoundException, TransactionStoppedException;
    NotificationM findById(Long id) throws NoRecordFoundException, TransactionStoppedException;
    NotificationM findBy(Long templateId) throws NoRecordFoundException, TransactionStoppedException;
    void save(NotificationM notificationM) throws TransactionStoppedException;
    void update(NotificationM notificationM) throws TransactionStoppedException;
    void delete(NotificationM notificationM) throws TransactionStoppedException;

}

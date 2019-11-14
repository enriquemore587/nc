package com.bbva.intranet.storage.domain.dao;

import com.bbva.intranet.storage.domain.model.NotificationSentM;
import com.bbva.intranet.not.core.exceptions.NoRecordFoundException;
import com.bbva.intranet.not.core.exceptions.TransactionStoppedException;

import java.util.List;

public interface NotCoreNotificationSentDAO {

    List<NotificationSentM> findAll() throws NoRecordFoundException, TransactionStoppedException;
    NotificationSentM findById(Long id) throws NoRecordFoundException, TransactionStoppedException;
    List<NotificationSentM> findByUserId(String userId) throws NoRecordFoundException, TransactionStoppedException;
    List<NotificationSentM> findBy(String userId, Long notificationId) throws NoRecordFoundException, TransactionStoppedException;
    List<NotificationSentM> findBy(String userId, Long notificationId, boolean read) throws NoRecordFoundException, TransactionStoppedException;
    List<NotificationSentM> findByNotificationId(Long notificationId) throws NoRecordFoundException, TransactionStoppedException;
    void save(NotificationSentM notificationSent) throws TransactionStoppedException;
    void update(NotificationSentM notificationSent) throws TransactionStoppedException;
    void delete(NotificationSentM notificationSent) throws TransactionStoppedException;

}
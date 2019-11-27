package com.bbva.intranet.storage.domain.dao;

import com.bbva.intranet.not.core.exceptions.NoRecordFoundException;
import com.bbva.intranet.not.core.exceptions.TransactionStoppedException;
import com.bbva.intranet.storage.domain.model.UserTopicM;

import java.util.List;

public interface NotCoreUserTopicDAO {

    List<UserTopicM> findByTopicId(Long topicId) throws NoRecordFoundException, TransactionStoppedException;
    List<UserTopicM> findByUserId(Long userId) throws NoRecordFoundException, TransactionStoppedException;
    UserTopicM findBy(Long userId, Long topicId) throws NoRecordFoundException, TransactionStoppedException;
    void save(UserTopicM userTopicM) throws TransactionStoppedException;
    void update(UserTopicM userTopicM) throws TransactionStoppedException;
    void delete(UserTopicM userTopicM) throws TransactionStoppedException;
}

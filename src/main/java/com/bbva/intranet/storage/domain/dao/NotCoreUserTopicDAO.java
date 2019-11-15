package com.bbva.intranet.storage.domain.dao;

import com.bbva.intranet.not.core.exceptions.TransactionStoppedException;
import com.bbva.intranet.storage.domain.model.UserTopicM;
import javassist.NotFoundException;

import java.util.List;

public interface NotCoreUserTopicDAO {

    List<UserTopicM> findByTopicId(Long topicId) throws NotFoundException, TransactionStoppedException;
    List<UserTopicM> findByUserId(Long userId) throws NotFoundException, TransactionStoppedException;
    UserTopicM findBy(Long userId, Long topicId) throws NotFoundException, TransactionStoppedException;
    void save(UserTopicM userTopicM) throws TransactionStoppedException;
    void update(UserTopicM userTopicM) throws TransactionStoppedException;
    void delete(UserTopicM userTopicM) throws TransactionStoppedException;
}

package com.bbva.intranet.storage.domain.dao;

import com.bbva.intranet.storage.domain.model.TopicM;
import com.bbva.intranet.not.core.exceptions.NoRecordFoundException;
import com.bbva.intranet.not.core.exceptions.TransactionStoppedException;

import java.util.List;

public interface NotCoreTopicDAO {

    List<TopicM> findAll() throws NoRecordFoundException, TransactionStoppedException;
    TopicM findById(Long id) throws NoRecordFoundException, TransactionStoppedException;
    TopicM findByName(String name) throws NoRecordFoundException, TransactionStoppedException;
    void save(TopicM topicM) throws TransactionStoppedException;
    void update(TopicM topicM) throws TransactionStoppedException;
    void delete(TopicM topicM) throws TransactionStoppedException;
}

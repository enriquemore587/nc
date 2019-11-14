package com.bbva.intranet.storage.domain.dao;

import com.bbva.intranet.storage.domain.model.UserM;
import com.bbva.intranet.not.core.exceptions.NoRecordFoundException;
import com.bbva.intranet.not.core.exceptions.TransactionStoppedException;

import java.util.List;

public interface NotCoreUserDAO {

    List<UserM> findAll() throws NoRecordFoundException, TransactionStoppedException;
    UserM findById(Long id) throws NoRecordFoundException, TransactionStoppedException;
    UserM findByEmail(String email) throws NoRecordFoundException, TransactionStoppedException;
    void save(UserM user) throws TransactionStoppedException;
    void update(UserM user) throws TransactionStoppedException;
    void delete(UserM user) throws TransactionStoppedException;

}

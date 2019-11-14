package com.bbva.intranet.storage.domain.dao;

import com.bbva.intranet.storage.domain.model.DeviceM;
import com.bbva.intranet.not.core.exceptions.NoRecordFoundException;
import com.bbva.intranet.not.core.exceptions.TransactionStoppedException;

import java.util.List;

public interface NotCoreDeviceDAO {

    List<DeviceM> findBy(String userId) throws NoRecordFoundException, TransactionStoppedException;
    DeviceM findById(Long id) throws NoRecordFoundException, TransactionStoppedException;
    void save(DeviceM deviceM) throws TransactionStoppedException;
    void update(DeviceM deviceM) throws TransactionStoppedException;
    void delete(DeviceM deviceM) throws TransactionStoppedException;

}

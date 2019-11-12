package com.bbva.intranet.not.core.domain.dao;

import com.bbva.intranet.not.core.domain.model.DeviceM;

import java.util.List;

public interface NotCoreDeviceDAO {
    List<DeviceM> findBy(String userId);
    DeviceM findById(Long id);
    void save(DeviceM deviceM);
    void update(DeviceM deviceM);
}

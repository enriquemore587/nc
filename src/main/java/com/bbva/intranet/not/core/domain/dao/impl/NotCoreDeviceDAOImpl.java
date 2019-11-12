package com.bbva.intranet.not.core.domain.dao.impl;

import com.bbva.intranet.not.core.domain.dao.NotCoreDeviceDAO;
import com.bbva.intranet.not.core.domain.model.DeviceM;
import org.hibernate.SessionFactory;

import java.util.List;

public class NotCoreDeviceDAOImpl implements NotCoreDeviceDAO {

    SessionFactory sessionFactory;

    @Override
    public List<DeviceM> findBy(String userId) {
        return null;
    }

    @Override
    public DeviceM findById(Long id) {
        return null;
    }

    @Override
    public void save(DeviceM deviceM) {

    }

    @Override
    public void update(DeviceM deviceM) {

    }
}

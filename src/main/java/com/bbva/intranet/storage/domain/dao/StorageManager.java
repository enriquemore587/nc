package com.bbva.intranet.storage.domain.dao;

import com.bbva.intranet.storage.exceptions.StorageException;

public interface StorageManager {
    void saveDevice(Object obj) throws StorageException;
    void saveNotification();
}

package com.bbva.intranet.storage.domain.dao.impl;

import com.bbva.intranet.not.core.utilities.NotCoreUtility;
import com.bbva.intranet.storage.domain.dao.NotCoreDeviceDAO;
import com.bbva.intranet.storage.domain.model.DeviceM;
import com.bbva.intranet.not.core.exceptions.NoRecordFoundException;
import com.bbva.intranet.not.core.exceptions.TransactionStoppedException;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static com.bbva.intranet.not.core.utilities.NotCoreUtility.STARTING;
import static com.bbva.intranet.not.core.utilities.NotCoreUtility.FINISHED;
import static com.bbva.intranet.not.core.utilities.NotCoreUtility.RECORD_NO_FOUND_MESSAGE;
import static com.bbva.intranet.not.core.utilities.NotCoreUtility.TRANSACTION_STOPPED_MESSAGE;

public class NotCoreDeviceDAOImpl implements NotCoreDeviceDAO {

    private static final Logger LOG = LoggerFactory.getLogger(NotCoreDeviceDAOImpl.class);

    private SessionFactory sessionFactory;

    public NotCoreDeviceDAOImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<DeviceM> findBy(String userId) throws NoRecordFoundException, TransactionStoppedException {
        LOG.info(STARTING);
        List<DeviceM> devices = null;
        Session session = null;
        try {
            session = sessionFactory.openSession();
            NotCoreUtility.enableEmojis(session);
            Criteria criteria = session.createCriteria(DeviceM.class)
                    .add(Restrictions.eq("userId", userId))
                    .setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
            devices = (List<DeviceM>) criteria.list();
            if (devices == null) throw new NoRecordFoundException(RECORD_NO_FOUND_MESSAGE);
        } catch (Exception e) {
            LOG.error(e.getMessage());
            throw new TransactionStoppedException(TRANSACTION_STOPPED_MESSAGE);
        } finally {
            if (session != null) session.close();
        }
        LOG.info(FINISHED);
        return devices;
    }

    @Override
    public DeviceM findById(Long id) throws NoRecordFoundException, TransactionStoppedException {
        LOG.info(STARTING);
        DeviceM device = null;
        Session session = null;
        try {
            session = sessionFactory.openSession();
            NotCoreUtility.enableEmojis(session);
            Criteria criteria = session.createCriteria(DeviceM.class)
                    .add(Restrictions.eq("id", id))
                    .setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
            device = (DeviceM) criteria.uniqueResult();
            if (device == null) throw new NoRecordFoundException(RECORD_NO_FOUND_MESSAGE);
        } catch (Exception e) {
            LOG.error(e.getMessage());
            throw new TransactionStoppedException(TRANSACTION_STOPPED_MESSAGE);
        } finally {
            if (session != null) session.close();
        }
        LOG.info(FINISHED);
        return device;
    }

    @Override
    public void save(DeviceM deviceM) throws TransactionStoppedException {
        LOG.info(STARTING);
        Lock lock = new ReentrantLock();
        lock.lock();
        Session session = null;
        Transaction tx = null;
        try {
            session = sessionFactory.openSession();
            NotCoreUtility.enableEmojis(session);
            tx = session.beginTransaction();
            session.save(deviceM);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            LOG.error(e.getMessage());
            throw new TransactionStoppedException(TRANSACTION_STOPPED_MESSAGE);
        } finally {
            if (session != null) session.close();
            lock.unlock();
        }
        LOG.info(FINISHED);
    }

    @Override
    public void update(DeviceM deviceM) throws TransactionStoppedException {
        LOG.info(STARTING);
        Lock lock = new ReentrantLock();
        lock.lock();
        Session session = null;
        Transaction tx = null;
        try {
            session = sessionFactory.openSession();
            NotCoreUtility.enableEmojis(session);
            tx = session.beginTransaction();
            session.update(deviceM);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            LOG.error(e.getMessage());
            throw new TransactionStoppedException(TRANSACTION_STOPPED_MESSAGE);
        } finally {
            if (session != null) session.close();
            lock.unlock();
        }
        LOG.info(FINISHED);
    }

    @Override
    public void delete(DeviceM deviceM) throws TransactionStoppedException {
        LOG.info(STARTING);
        Lock lock = new ReentrantLock();
        lock.lock();
        Session session = null;
        Transaction tx = null;
        try {
            session = sessionFactory.openSession();
            NotCoreUtility.enableEmojis(session);
            tx = session.beginTransaction();
            session.delete(deviceM);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            LOG.error(e.getMessage());
            throw new TransactionStoppedException(TRANSACTION_STOPPED_MESSAGE);
        } finally {
            if (session != null) session.close();
            lock.unlock();
        }
        LOG.info(FINISHED);
    }
}

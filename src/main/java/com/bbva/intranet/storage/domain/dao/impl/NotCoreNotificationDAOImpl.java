package com.bbva.intranet.storage.domain.dao.impl;

import com.bbva.intranet.not.core.utilities.NotCoreUtility;
import com.bbva.intranet.storage.domain.dao.NotCoreNotificationDAO;
import com.bbva.intranet.storage.domain.model.NotificationM;
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


import static com.bbva.intranet.not.core.utilities.NotCoreUtility.FINISHED;
import static com.bbva.intranet.not.core.utilities.NotCoreUtility.STARTING;
import static com.bbva.intranet.not.core.utilities.NotCoreUtility.RECORD_NO_FOUND_MESSAGE;
import static com.bbva.intranet.not.core.utilities.NotCoreUtility.TRANSACTION_STOPPED_MESSAGE;

public class NotCoreNotificationDAOImpl implements NotCoreNotificationDAO {

    private static final Logger LOG = LoggerFactory.getLogger(NotCoreNotificationDAOImpl.class);

    private SessionFactory sessionFactory;

    public NotCoreNotificationDAOImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<NotificationM> findAll() throws NoRecordFoundException, TransactionStoppedException {
        LOG.info(STARTING);
        List<NotificationM> notifications = null;
        Session session = null;
        try {
            session = sessionFactory.openSession();
            NotCoreUtility.enableEmojis(session);
            Criteria criteria = session.createCriteria(NotificationM.class)
                    .setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
            notifications = (List<NotificationM>) criteria.list();
            if (notifications == null) throw new NoRecordFoundException(RECORD_NO_FOUND_MESSAGE);
        } catch (Exception e) {
            LOG.error(e.getMessage());
            throw new TransactionStoppedException(TRANSACTION_STOPPED_MESSAGE);
        } finally {
            if (session != null) session.close();
        }
        LOG.info(FINISHED);
        return notifications;
    }

    @Override
    public NotificationM findById(Long id) throws NoRecordFoundException, TransactionStoppedException {
        LOG.info(STARTING);
        NotificationM notification = null;
        Session session = null;
        try {
            session = sessionFactory.openSession();
            NotCoreUtility.enableEmojis(session);
            Criteria criteria = session.createCriteria(NotificationM.class)
                    .add(Restrictions.eq("id", id))
                    .setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
            notification = (NotificationM) criteria.uniqueResult();
            if (notification == null) throw new NoRecordFoundException(RECORD_NO_FOUND_MESSAGE);
        } catch (Exception e) {
            LOG.error(e.getMessage());
            throw new TransactionStoppedException(TRANSACTION_STOPPED_MESSAGE);
        } finally {
            if (session != null) session.close();
        }
        LOG.info(FINISHED);
        return notification;
    }

    @Override
    public NotificationM findBy(Long templateId) throws NoRecordFoundException, TransactionStoppedException {

        LOG.info(STARTING);
        NotificationM notification = null;
        Session session = null;
        try {
            session = sessionFactory.openSession();
            NotCoreUtility.enableEmojis(session);
            Criteria criteria = session.createCriteria(NotificationM.class)
                    .add(Restrictions.eq("templateId", templateId))
                    .setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
            notification = (NotificationM) criteria.uniqueResult();
            if (notification == null) throw new NoRecordFoundException(RECORD_NO_FOUND_MESSAGE);
        } catch (Exception e) {
            LOG.error(e.getMessage());
            throw new TransactionStoppedException(TRANSACTION_STOPPED_MESSAGE);
        } finally {
            if (session != null) session.close();
        }
        LOG.info(FINISHED);
        return notification;
    }

    @Override
    public void save(NotificationM notificationM) throws TransactionStoppedException {
        LOG.info(STARTING);
        Lock lock = new ReentrantLock();
        lock.lock();
        Session session = null;
        Transaction tx = null;
        try {
            session = sessionFactory.openSession();
            NotCoreUtility.enableEmojis(session);
            tx = session.beginTransaction();
            session.save(notificationM);
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
    public void update(NotificationM notificationM) throws TransactionStoppedException {
        LOG.info(STARTING);
        Lock lock = new ReentrantLock();
        lock.lock();
        Session session = null;
        Transaction tx = null;
        try {
            session = sessionFactory.openSession();
            NotCoreUtility.enableEmojis(session);
            tx = session.beginTransaction();
            session.update(notificationM);
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
    public void delete(NotificationM notificationM) throws TransactionStoppedException {
        LOG.info(STARTING);
        Lock lock = new ReentrantLock();
        lock.lock();
        Session session = null;
        Transaction tx = null;
        try {
            session = sessionFactory.openSession();
            NotCoreUtility.enableEmojis(session);
            tx = session.beginTransaction();
            session.delete(notificationM);
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

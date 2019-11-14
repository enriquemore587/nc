package com.bbva.intranet.storage.domain.dao.impl;

import com.bbva.intranet.not.core.utilities.NotCoreUtility;
import com.bbva.intranet.storage.domain.dao.NotCoreNotificationSentDAO;
import com.bbva.intranet.storage.domain.model.NotificationSentM;
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

public class NotCoreNotificationSentDAOImpl implements NotCoreNotificationSentDAO {

    private static final Logger LOG = LoggerFactory.getLogger(NotCoreNotificationSentDAOImpl.class);

    private SessionFactory sessionFactory;

    public NotCoreNotificationSentDAOImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<NotificationSentM> findAll() throws NoRecordFoundException, TransactionStoppedException {
        LOG.info(STARTING);
        List<NotificationSentM> notifications = null;
        Session session = null;
        try {
            session = sessionFactory.openSession();
            NotCoreUtility.enableEmojis(session);
            Criteria criteria = session.createCriteria(NotificationSentM.class)
                    .setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
            notifications = (List<NotificationSentM>) criteria.list();
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
    public NotificationSentM findById(Long id) throws NoRecordFoundException, TransactionStoppedException {
        LOG.info(STARTING);
        NotificationSentM notificationSent = null;
        Session session = null;
        try {
            session = sessionFactory.openSession();
            NotCoreUtility.enableEmojis(session);
            Criteria criteria = session.createCriteria(NotificationSentM.class)
                    .add(Restrictions.eq("id", id))
                    .setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
            notificationSent = (NotificationSentM) criteria.uniqueResult();
            if (notificationSent == null) throw new NoRecordFoundException(RECORD_NO_FOUND_MESSAGE);
        } catch (Exception e) {
            LOG.error(e.getMessage());
            throw new TransactionStoppedException(TRANSACTION_STOPPED_MESSAGE);
        } finally {
            if (session != null) session.close();
        }
        LOG.info(FINISHED);
        return notificationSent;
    }

    @Override
    public List<NotificationSentM> findByUserId(String userId) throws NoRecordFoundException, TransactionStoppedException {
        LOG.info(STARTING);
        List<NotificationSentM> notifications = null;
        Session session = null;
        try {
            session = sessionFactory.openSession();
            NotCoreUtility.enableEmojis(session);
            Criteria criteria = session.createCriteria(NotificationSentM.class)
                    .add(Restrictions.eq("userId", userId))
                    .setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
            notifications = (List<NotificationSentM>) criteria.list();
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
    public List<NotificationSentM> findBy(String userId, Long notificationId) throws NoRecordFoundException, TransactionStoppedException {
        LOG.info(STARTING);
        List<NotificationSentM> notifications = null;
        Session session = null;
        try {
            session = sessionFactory.openSession();
            NotCoreUtility.enableEmojis(session);
            Criteria criteria = session.createCriteria(NotificationSentM.class)
                    .add(Restrictions.and(
                            Restrictions.eq("userId", userId),
                            Restrictions.eq("notificationId", notificationId)
                    ))
                    .setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
            notifications = (List<NotificationSentM>) criteria.list();
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
    public List<NotificationSentM> findBy(String userId, Long notificationId, boolean read) throws NoRecordFoundException, TransactionStoppedException {
        LOG.info(STARTING);
        List<NotificationSentM> notifications = null;
        Session session = null;
        try {
            session = sessionFactory.openSession();
            NotCoreUtility.enableEmojis(session);
            Criteria criteria = session.createCriteria(NotificationSentM.class)
                    .add(Restrictions.and(
                            Restrictions.eq("userId", userId),
                            Restrictions.eq("notificationId", notificationId),
                            Restrictions.eq("read", read)
                    ))
                    .setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
            notifications = (List<NotificationSentM>) criteria.list();
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
    public List<NotificationSentM> findByNotificationId(Long notificationId) throws NoRecordFoundException, TransactionStoppedException {
        LOG.info(STARTING);
        List<NotificationSentM> notifications = null;
        Session session = null;
        try {
            session = sessionFactory.openSession();
            NotCoreUtility.enableEmojis(session);
            Criteria criteria = session.createCriteria(NotificationSentM.class)
                    .add(Restrictions.eq("notificationId", notificationId))
                    .setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
            notifications = (List<NotificationSentM>) criteria.list();
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
    public void save(NotificationSentM notificationSent) throws TransactionStoppedException {
        LOG.info(STARTING);
        Lock lock = new ReentrantLock();
        lock.lock();
        Session session = null;
        Transaction tx = null;
        try {
            session = sessionFactory.openSession();
            NotCoreUtility.enableEmojis(session);
            tx = session.beginTransaction();
            session.save(notificationSent);
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
    public void update(NotificationSentM notificationSent) throws TransactionStoppedException {
        LOG.info(STARTING);
        Lock lock = new ReentrantLock();
        lock.lock();
        Session session = null;
        Transaction tx = null;
        try {
            session = sessionFactory.openSession();
            NotCoreUtility.enableEmojis(session);
            tx = session.beginTransaction();
            session.update(notificationSent);
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
    public void delete(NotificationSentM notificationSent) throws TransactionStoppedException {
        LOG.info(STARTING);
        Lock lock = new ReentrantLock();
        lock.lock();
        Session session = null;
        Transaction tx = null;
        try {
            session = sessionFactory.openSession();
            NotCoreUtility.enableEmojis(session);
            tx = session.beginTransaction();
            session.delete(notificationSent);
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

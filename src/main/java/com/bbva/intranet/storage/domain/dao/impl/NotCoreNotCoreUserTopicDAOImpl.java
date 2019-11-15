package com.bbva.intranet.storage.domain.dao.impl;

import com.bbva.intranet.not.core.exceptions.NoRecordFoundException;
import com.bbva.intranet.not.core.exceptions.TransactionStoppedException;
import com.bbva.intranet.not.core.utilities.NotCoreUtility;
import com.bbva.intranet.storage.domain.dao.NotCoreUserTopicDAO;
import com.bbva.intranet.storage.domain.model.UserTopicM;
import javassist.NotFoundException;
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

import static com.bbva.intranet.not.core.utilities.NotCoreUtility.RECORD_NO_FOUND_MESSAGE;
import static com.bbva.intranet.not.core.utilities.NotCoreUtility.UNCONTROLLED_ERROR_MESSAGE;
import static com.bbva.intranet.not.core.utilities.NotCoreUtility.TRANSACTION_STOPPED_MESSAGE;
import static com.bbva.intranet.not.core.utilities.NotCoreUtility.STARTING;
import static com.bbva.intranet.not.core.utilities.NotCoreUtility.FINISHED;

public class NotCoreNotCoreUserTopicDAOImpl implements NotCoreUserTopicDAO {

    private static final Logger LOG = LoggerFactory.getLogger(NotCoreDeviceDAOImpl.class);

    private SessionFactory sessionFactory;

    public NotCoreNotCoreUserTopicDAOImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<UserTopicM> findByTopicId(Long topicId) throws NotFoundException, TransactionStoppedException {
        LOG.info(STARTING);
        List<UserTopicM> userTopicMS = null;
        Session session = null;
        try {
            session = sessionFactory.openSession();
            NotCoreUtility.enableEmojis(session);
            Criteria criteria = session.createCriteria(UserTopicM.class)
                    .add(Restrictions.eq("topicId", topicId))
                    .setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
            userTopicMS = (List<UserTopicM>) criteria.list();
            if (userTopicMS == null) throw new NoRecordFoundException(RECORD_NO_FOUND_MESSAGE);
        } catch (Exception e) {
            LOG.error(e.getMessage());
            throw new TransactionStoppedException(UNCONTROLLED_ERROR_MESSAGE);
        } finally {
            if (session != null) session.close();
        }
        LOG.info(FINISHED);
        return userTopicMS;
    }

    @Override
    public List<UserTopicM> findByUserId(Long userId) throws NotFoundException, TransactionStoppedException {
        LOG.info(STARTING);
        List<UserTopicM> userTopicMS = null;
        Session session = null;
        try {
            session = sessionFactory.openSession();
            NotCoreUtility.enableEmojis(session);
            Criteria criteria = session.createCriteria(UserTopicM.class)
                    .add(Restrictions.eq("userId", userId))
                    .setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
            userTopicMS = (List<UserTopicM>) criteria.list();
            if (userTopicMS == null) throw new NoRecordFoundException(RECORD_NO_FOUND_MESSAGE);
        } catch (Exception e) {
            LOG.error(e.getMessage());
            throw new TransactionStoppedException(UNCONTROLLED_ERROR_MESSAGE);
        } finally {
            if (session != null) session.close();
        }
        LOG.info(FINISHED);
        return userTopicMS;
    }

    @Override
    public UserTopicM findBy(Long userId, Long topicId) throws NotFoundException, TransactionStoppedException {
        LOG.info(STARTING);
        UserTopicM userTopicM = null;
        Session session = null;
        try {
            session = sessionFactory.openSession();
            NotCoreUtility.enableEmojis(session);
            Criteria criteria = session.createCriteria(UserTopicM.class)
                    .add(
                            Restrictions.and(
                                Restrictions.eq("userId", userId),
                                Restrictions.eq("topicId", topicId)
                            )
                    )
                    .setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
            userTopicM = (UserTopicM) criteria.uniqueResult();
            if (userTopicM == null) throw new NoRecordFoundException(RECORD_NO_FOUND_MESSAGE);
        } catch (Exception e) {
            LOG.error(e.getMessage());
            throw new TransactionStoppedException(UNCONTROLLED_ERROR_MESSAGE);
        } finally {
            if (session != null) session.close();
        }
        LOG.info(FINISHED);
        return userTopicM;
    }

    @Override
    public void save(UserTopicM userTopicM) throws TransactionStoppedException {
        LOG.info(STARTING);
        Lock lock = new ReentrantLock();
        lock.lock();
        Session session = null;
        Transaction tx = null;
        try {
            session = sessionFactory.openSession();
            tx = session.beginTransaction();
            NotCoreUtility.enableEmojis(session);
            session.save(userTopicM);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            LOG.error(e.getMessage());
            throw new TransactionStoppedException(TRANSACTION_STOPPED_MESSAGE);
        } finally {
            session.close();
            lock.unlock();
        }
        LOG.info(FINISHED);
    }

    @Override
    public void update(UserTopicM userTopicM) throws TransactionStoppedException {
        LOG.info(STARTING);
        Lock lock = new ReentrantLock();
        lock.lock();
        Session session = null;
        Transaction tx = null;
        try {
            session = sessionFactory.openSession();
            tx = session.beginTransaction();
            NotCoreUtility.enableEmojis(session);
            session.update(userTopicM);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            LOG.error(e.getMessage());
            throw new TransactionStoppedException(TRANSACTION_STOPPED_MESSAGE);
        } finally {
            session.close();
            lock.unlock();
        }
        LOG.info(FINISHED);
    }

    @Override
    public void delete(UserTopicM userTopicM) throws TransactionStoppedException {
        LOG.info(STARTING);
        Lock lock = new ReentrantLock();
        lock.lock();
        Session session = null;
        Transaction tx = null;
        try {
            session = sessionFactory.openSession();
            tx = session.beginTransaction();
            NotCoreUtility.enableEmojis(session);
            session.delete(userTopicM);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            LOG.error(e.getMessage());
            throw new TransactionStoppedException(TRANSACTION_STOPPED_MESSAGE);
        } finally {
            session.close();
            lock.unlock();
        }
        LOG.info(FINISHED);
    }
}

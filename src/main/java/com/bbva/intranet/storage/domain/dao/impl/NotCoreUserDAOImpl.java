package com.bbva.intranet.storage.domain.dao.impl;

import com.bbva.intranet.not.core.utilities.NotCoreUtility;
import com.bbva.intranet.storage.domain.dao.NotCoreUserDAO;
import com.bbva.intranet.storage.domain.model.UserM;
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
import static com.bbva.intranet.not.core.utilities.NotCoreUtility.UNCONTROLLED_ERROR_MESSAGE;

public class NotCoreUserDAOImpl implements NotCoreUserDAO {

    private static final Logger LOG = LoggerFactory.getLogger(NotCoreUserDAOImpl.class);

    private SessionFactory sessionFactory;

    public NotCoreUserDAOImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<UserM> findAll() throws NoRecordFoundException, TransactionStoppedException {
        LOG.info(STARTING);
        List<UserM> userMList = null;
        Session session = null;
        try {
            session = sessionFactory.openSession();
            NotCoreUtility.enableEmojis(session);
            Criteria criteria = session.createCriteria(UserM.class)
                    .setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
            userMList = (List<UserM>) criteria.list();
            if (userMList == null) throw new NoRecordFoundException(RECORD_NO_FOUND_MESSAGE);
        } catch (Exception e) {
            LOG.error(e.getMessage());
            throw new TransactionStoppedException(UNCONTROLLED_ERROR_MESSAGE);
        } finally {
            if (session != null)session.close();
        }
        LOG.info(FINISHED);
        return userMList;
    }

    @Override
    public UserM findById(Long id) throws NoRecordFoundException, TransactionStoppedException {
        LOG.info(STARTING);
        UserM user= null;
        Session session = null;
        try {
            session = sessionFactory.openSession();
            NotCoreUtility.enableEmojis(session);
            Criteria criteria = session.createCriteria(UserM.class)
                    .add(Restrictions.eq("id", id))
                    .setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
            user = (UserM) criteria.uniqueResult();
            if (user == null) throw new NoRecordFoundException(RECORD_NO_FOUND_MESSAGE);
        } catch (Exception e) {
            LOG.error(e.getMessage());
            throw new TransactionStoppedException(UNCONTROLLED_ERROR_MESSAGE);
        } finally {
            if (session != null) session.close();
        }
        LOG.info(FINISHED);
        return user;
    }

    @Override
    public UserM findByEmail(String email) throws NoRecordFoundException, TransactionStoppedException {
        LOG.info(STARTING);
        UserM user = null;
        Session session = null;
        try {
            session = sessionFactory.openSession();
            NotCoreUtility.enableEmojis(session);
            Criteria criteria = session.createCriteria(UserM.class)
                    .add(Restrictions.eq("email", email))
                    .setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
            user = (UserM) criteria.uniqueResult();
            if (user == null) throw new NoRecordFoundException(RECORD_NO_FOUND_MESSAGE);
        } catch (Exception e) {
            LOG.error(e.getMessage());
            throw new TransactionStoppedException(UNCONTROLLED_ERROR_MESSAGE);
        } finally {
            if (session != null) session.close();
        }
        LOG.info(FINISHED);
        return user;
    }

    @Override
    public void save(UserM user) throws TransactionStoppedException {
        LOG.info(STARTING);
        Lock lock = new ReentrantLock();
        lock.lock();
        Session session = null;
        Transaction tx = null;
        try {
            session = sessionFactory.openSession();
            NotCoreUtility.enableEmojis(session);
            tx = session.beginTransaction();
            session.save(user);
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
    public void update(UserM user) throws TransactionStoppedException {
        LOG.info(STARTING);
        Lock lock = new ReentrantLock();
        lock.lock();
        Session session = null;
        Transaction tx = null;
        try {
            session = sessionFactory.openSession();
            tx = session.beginTransaction();
            NotCoreUtility.enableEmojis(session);
            session.update(user);
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
    public void delete(UserM user) throws TransactionStoppedException {
        LOG.info(STARTING);
        Lock lock = new ReentrantLock();
        lock.lock();
        Session session = null;
        Transaction tx = null;
        try {
            session = sessionFactory.openSession();
            NotCoreUtility.enableEmojis(session);
            tx = session.beginTransaction();
            session.delete(user);
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

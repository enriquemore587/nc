package com.bbva.intranet.storage.domain.dao.impl;

import com.bbva.intranet.not.core.utilities.NotCoreUtility;
import com.bbva.intranet.storage.domain.dao.NotCoreTopicDAO;
import com.bbva.intranet.storage.domain.model.TopicM;
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

public class NotCoreTopicDAOImpl implements NotCoreTopicDAO {

    private static final Logger LOG = LoggerFactory.getLogger(NotCoreTopicDAOImpl.class);

    private SessionFactory sessionFactory;

    public NotCoreTopicDAOImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<TopicM> findAll() throws NoRecordFoundException, TransactionStoppedException {
        LOG.info(STARTING);
        List<TopicM> topics = null;
        Session session = null;
        try {
            session = sessionFactory.openSession();
            NotCoreUtility.enableEmojis(session);
            Criteria criteria = session.createCriteria(TopicM.class)
                    .setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
            topics = (List<TopicM>) criteria.list();
        } catch (Exception e) {
            LOG.error(e.getMessage());
            throw new TransactionStoppedException(UNCONTROLLED_ERROR_MESSAGE);
        } finally {
            if (session != null) session.close();
        }
        if (topics == null) throw new NoRecordFoundException(RECORD_NO_FOUND_MESSAGE);
        LOG.info(FINISHED);
        return topics;
    }

    @Override
    public TopicM findById(Long id) throws NoRecordFoundException, TransactionStoppedException {
        LOG.info(STARTING);
        TopicM topic = null;
        Session session = null;
        try {
            session = sessionFactory.openSession();
            NotCoreUtility.enableEmojis(session);
            Criteria criteria = session.createCriteria(TopicM.class)
                    .add(Restrictions.eq("id", id))
                    .setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
            topic = (TopicM) criteria.uniqueResult();
        } catch (Exception e) {
            LOG.error(e.getMessage());
            throw new TransactionStoppedException(UNCONTROLLED_ERROR_MESSAGE);
        } finally {
            if (session != null) session.close();
        }
        if (topic == null) throw new NoRecordFoundException(RECORD_NO_FOUND_MESSAGE);
        LOG.info(FINISHED);
        return topic;
    }

    @Override
    public TopicM findByName(String name) throws NoRecordFoundException, TransactionStoppedException {
        LOG.info(STARTING);
        TopicM topic = null;
        Session session = null;
        try {
            session = sessionFactory.openSession();
            NotCoreUtility.enableEmojis(session);
            Criteria criteria = session.createCriteria(TopicM.class)
                    .add(Restrictions.eq("name", name))
                    .setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
            topic = (TopicM) criteria.uniqueResult();
        } catch (Exception e) {
            LOG.error(e.getMessage());
            throw new TransactionStoppedException(UNCONTROLLED_ERROR_MESSAGE);
        } finally {
            if (session != null) session.close();
        }
        if (topic == null) throw new NoRecordFoundException(RECORD_NO_FOUND_MESSAGE);
        LOG.info(FINISHED);
        return topic;
    }

    @Override
    public void save(TopicM topicM) throws TransactionStoppedException {
        LOG.info(STARTING);
        Lock lock = new ReentrantLock();
        lock.lock();
        Session session = null;
        Transaction tx = null;
        try {
            session = sessionFactory.openSession();
            NotCoreUtility.enableEmojis(session);
            tx = session.beginTransaction();
            session.save(topicM);
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
    public void update(TopicM topicM) throws TransactionStoppedException {
        LOG.info(STARTING);
        Lock lock = new ReentrantLock();
        lock.lock();
        Session session = null;
        Transaction tx = null;
        try {
            session = sessionFactory.openSession();
            NotCoreUtility.enableEmojis(session);
            tx = session.beginTransaction();
            session.update(topicM);
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
    public void delete(TopicM topicM) throws TransactionStoppedException {
        LOG.info(STARTING);
        Lock lock = new ReentrantLock();
        lock.lock();
        Session session = null;
        Transaction tx = null;
        try {
            session = sessionFactory.openSession();
            NotCoreUtility.enableEmojis(session);
            tx = session.beginTransaction();
            session.delete(topicM);
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
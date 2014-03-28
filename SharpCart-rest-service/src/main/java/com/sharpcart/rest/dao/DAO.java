package com.sharpcart.rest.dao;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

public class DAO {

    private static final Logger log = Logger.getAnonymousLogger();
    
    private static final ThreadLocal<Session> session = new ThreadLocal<Session>();
    private static SessionFactory sessionFactory;
    
    private static final DAO instance = new DAO();
    
    private DAO() {
		final Configuration configuration = new Configuration ().configure();
		final StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder().
		applySettings(configuration.getProperties());
		sessionFactory = configuration.buildSessionFactory(builder.build());
    }

    public static DAO getInstance() {
    	return instance;
    }
    
    public Session getSession() {
        Session session = DAO.session.get();
        if (session == null) {
            session = sessionFactory.openSession();
            DAO.session.set(session);
        }
        
        return session;
    }

    public void begin() {
        getSession().beginTransaction();
    }

    public void commit() {
        getSession().getTransaction().commit();
    }

    public void rollback() {
        try {
            getSession().getTransaction().rollback();
        } catch (final HibernateException e) {
            log.log(Level.WARNING, "Cannot rollback", e);
        }

        try {
            getSession().close();
        } catch (final HibernateException e) {
            log.log(Level.WARNING, "Cannot close", e);
        }
        
        DAO.session.set(null);
    }

    public void close() {
        getSession().close();
        DAO.session.set(null);
    }

}

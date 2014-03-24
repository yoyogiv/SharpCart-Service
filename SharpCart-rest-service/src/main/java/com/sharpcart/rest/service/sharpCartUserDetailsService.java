package com.sharpcart.rest.service;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.sharpcart.rest.controller.UserManagementController;
import com.sharpcart.rest.model.sharpCartUserDetails;
import com.sharpcart.rest.persistence.model.SharpCartUser;

@Component ("userService")
public class sharpCartUserDetailsService implements UserDetailsService {
	private static Logger LOG = LoggerFactory.getLogger(sharpCartUserDetailsService.class);
	
	private SessionFactory factory;
	private Session session;

	@Override
	public UserDetails loadUserByUsername(String username)
			throws UsernameNotFoundException {
		
		//debug
		LOG.info("User Name: "+username); //name
		
		//Find user in database
		Configuration configuration = new AnnotationConfiguration ().configure();
		StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder().
		applySettings(configuration.getProperties());
		factory = configuration.buildSessionFactory(builder.build());
		
		session = factory.openSession();
		
		session.beginTransaction();
		Query query = session.createQuery("from SharpCartUser where userName = :userName");
		query.setString("userName", username);
		SharpCartUser user = (SharpCartUser)query.uniqueResult();
		session.getTransaction().commit();
		session.close();
		
		if (user==null)
		{
			throw new UsernameNotFoundException("User name not found");
		}
		
		return new sharpCartUserDetails(user);
	}

}

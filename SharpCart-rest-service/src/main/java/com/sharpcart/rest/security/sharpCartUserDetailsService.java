package com.sharpcart.rest.security;

import java.util.ArrayList;
import java.util.Collection;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.sharpcart.rest.dao.DAO;
import com.sharpcart.rest.persistence.model.SharpCartUser;

@Component
public class sharpCartUserDetailsService implements UserDetailsService {
	private static Logger LOG = LoggerFactory.getLogger(sharpCartUserDetailsService.class);

	private final Collection<GrantedAuthority> grantedAuthorities = new ArrayList<GrantedAuthority>();
	
	public sharpCartUserDetailsService()
	{
		super();
	}
	
	@Override
	public UserDetails loadUserByUsername(String username)
			throws UsernameNotFoundException {
		
		//debug
		LOG.info("User Name: "+username); //name
		SharpCartUser user = null;
		
		//Find user in database
		try {
			DAO.getInstance().begin();
			final Query query = DAO.getInstance().getSession().createQuery("from SharpCartUser where userName = :userName");
			query.setString("userName", username);
			user = (SharpCartUser)query.uniqueResult();
			DAO.getInstance().commit();
		} catch (final HibernateException ex)
		{
			DAO.getInstance().rollback();
			ex.printStackTrace();
		}
		
		DAO.getInstance().close();
		
		if (user==null)
		{
			throw new UsernameNotFoundException("User name not found");
		}
		
		return new sharpCartUserDetails(user);
	}
}

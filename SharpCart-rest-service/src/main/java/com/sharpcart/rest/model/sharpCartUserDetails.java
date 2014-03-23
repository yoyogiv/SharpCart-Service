package com.sharpcart.rest.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.sharpcart.rest.persistence.model.SharpCartUser;

public class sharpCartUserDetails implements UserDetails {

	public static final String SCOPE_READ = "read";

	public static final String SCOPE_WRITE = "write";

	public static final String ROLE_USER = "USER";

	private Collection<GrantedAuthority> grantedAuthorities = new ArrayList<GrantedAuthority>();
	
	private SharpCartUser user;
	
	/*
	 * A constructor that accepts a SharpCartUser
	 */
	public sharpCartUserDetails(SharpCartUser user)
	{
		Assert.notNull(user,"user object cannot be null");
		this.user = user;
		
		//create a simple granted authority for each of our authorities and add it to the list of granted authorities
		for (String ga : Arrays.asList(ROLE_USER, SCOPE_READ, SCOPE_WRITE))
		{
			grantedAuthorities.add(new SimpleGrantedAuthority(ga));
		}
		
	}
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return grantedAuthorities;
	}

	@Override
	public String getPassword() {
		return user.getPassword();
	}

	@Override
	public String getUsername() {
		return user.getUserName();
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

}

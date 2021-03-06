package com.sharpcart.rest.security;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.Assert;

import com.sharpcart.rest.persistence.model.SharpCartUser;

public class sharpCartUserDetails implements UserDetails {

	public static final String SCOPE_READ = "read";

	public static final String SCOPE_WRITE = "write";
	
	public static final String ROLE_USER = "ROLE_USER";

	private final Collection<GrantedAuthority> grantedAuthorities = new ArrayList<GrantedAuthority>();
	
	private final SharpCartUser user;
	
	private static final long serialVersionUID = -6509897037222767090L;
	
	/*
	 * A constructor that accepts a SharpCartUser
	 */
	public sharpCartUserDetails(SharpCartUser user)
	{
		Assert.notNull(user,"user object cannot be null");
		
		this.user = user;
		for (final String ga : Arrays.asList(ROLE_USER, SCOPE_READ,SCOPE_WRITE))
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
		return isEnabled();
	}

	@Override
	public boolean isAccountNonLocked() {
		return isEnabled();
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return isEnabled();
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

}

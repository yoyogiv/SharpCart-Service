package com.sharpcart.rest.security;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.authentication.dao.SaltSource;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.security.authentication.encoding.PlaintextPasswordEncoder;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.Assert;

import com.sharpcart.rest.dao.DAO;
import com.sharpcart.rest.persistence.model.SharpCartUser;
import com.sharpcart.rest.utilities.PasswordHash;

public class sharpCartAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {

	private static Logger LOG = LoggerFactory.getLogger(sharpCartAuthenticationProvider.class);
    // ~ Instance fields ================================================================================================

    private PasswordEncoder passwordEncoder = new PlaintextPasswordEncoder();

    private SaltSource saltSource;

    public sharpCartAuthenticationProvider() {
        super();
    }

    // ~ Methods ========================================================================================================

    @Override
    @SuppressWarnings("deprecation")
    protected void additionalAuthenticationChecks(final UserDetails userDetails, final UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
        Object salt = null;

        if (saltSource != null) {
            salt = saltSource.getSalt(userDetails);
        }

        if (authentication.getCredentials() == null) {
            logger.debug("Authentication failed: no credentials provided");

            throw new BadCredentialsException(messages.getMessage("AbstractUserDetailsAuthenticationProvider.badCredentials", "Bad credentials"), userDetails);
        }

        final String presentedPassword = authentication.getCredentials().toString();

        if (!passwordEncoder.isPasswordValid(userDetails.getPassword(), presentedPassword, salt)) {
            logger.debug("Authentication failed: password does not match stored value");

            throw new BadCredentialsException(messages.getMessage("AbstractUserDetailsAuthenticationProvider.badCredentials", "Bad credentials"), userDetails);
        }
    }

    @Override
    protected final UserDetails retrieveUser(final String name, final UsernamePasswordAuthenticationToken authentication) {
        final String password = authentication.getCredentials().toString();

		//debug
		LOG.debug("User Name: "+name); //name
		LOG.debug("User Password: "+password);//password
		
		SharpCartUser user = null;
		try {
		//Find user in database
		try {
			DAO.getInstance().begin();
			final Query query = DAO.getInstance().getSession().createQuery("from SharpCartUser where userName = :userName");
			query.setString("userName", name);
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
			LOG.debug("user authentication failure - no user in db");
			
            // temporary - the idea here is to generate the not authorized exception - not by hand, but by returning wrong credentials which in turn will be refused later
            return new org.springframework.security.core.userdetails.User("wrongUsername", "wrongPass", new ArrayList<GrantedAuthority>());
		} else //check password
		{
	  		try {
				if (PasswordHash.validatePassword(password, user.getPassword()))
				{
					LOG.info("user authentication success");
					return new sharpCartUserDetails(user);
				} else // user is in database but provided password is incorrect
				{
					LOG.info("user authentication failure - wrong password");
		            // temporary - the idea here is to generate the not authorized exception - not by hand, but by returning wrong credentials which in turn will be refused later
		            return new org.springframework.security.core.userdetails.User("wrongUsername", "wrongPass", new ArrayList<GrantedAuthority>());
				}
			} catch (final NoSuchAlgorithmException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
	            // temporary - the idea here is to generate the not authorized exception - not by hand, but by returning wrong credentials which in turn will be refused later
	            return new org.springframework.security.core.userdetails.User("wrongUsername", "wrongPass", new ArrayList<GrantedAuthority>());
				
			} catch (final InvalidKeySpecException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
	            // temporary - the idea here is to generate the not authorized exception - not by hand, but by returning wrong credentials which in turn will be refused later
	            return new org.springframework.security.core.userdetails.User("wrongUsername", "wrongPass", new ArrayList<GrantedAuthority>());
			}
		}
		} catch (final Exception ex) {
            throw new AuthenticationServiceException(ex.getMessage(), ex);
		}
    }

    /**
     * Sets the PasswordEncoder instance to be used to encode and validate passwords. If not set, the password will be compared as plain text.
     * <p/>
     * For systems which are already using salted password which are encoded with a previous release, the encoder should be of type {@code org.springframework.security.authentication.encoding.PasswordEncoder}. Otherwise, the recommended
     * approach is to use {@code org.springframework.security.crypto.password.PasswordEncoder}.
     * 
     * @param passwordEncoderToSet
     *            must be an instance of one of the {@code PasswordEncoder} types.
     */
    public void setPasswordEncoder(final Object passwordEncoderToSet) {
        Assert.notNull(passwordEncoderToSet, "passwordEncoder cannot be null");

        if (passwordEncoderToSet instanceof PasswordEncoder) {
            passwordEncoder = (PasswordEncoder) passwordEncoderToSet;
            return;
        }

        if (passwordEncoderToSet instanceof org.springframework.security.crypto.password.PasswordEncoder) {
            final org.springframework.security.crypto.password.PasswordEncoder delegate = (org.springframework.security.crypto.password.PasswordEncoder) passwordEncoderToSet;
            passwordEncoder = new PasswordEncoder() {
                @Override
                public String encodePassword(final String rawPass, final Object salt) {
                    checkSalt(salt);
                    return delegate.encode(rawPass);
                }

                @Override
                public boolean isPasswordValid(final String encPass, final String rawPass, final Object salt) {
                    checkSalt(salt);
                    return delegate.matches(rawPass, encPass);
                }

                private void checkSalt(final Object salt) {
                    Assert.isNull(salt, "Salt value must be null when used with crypto module PasswordEncoder");
                }
            };

            return;
        }

        throw new IllegalArgumentException("passwordEncoder must be a PasswordEncoder instance");
    }

    protected PasswordEncoder getPasswordEncoder() {
        return passwordEncoder;
    }

    /**
     * The source of salts to use when decoding passwords. <code>null</code> is a valid value, meaning the <code>DaoAuthenticationProvider</code> will present <code>null</code> to the relevant <code>PasswordEncoder</code>.
     * <p/>
     * Instead, it is recommended that you use an encoder which uses a random salt and combines it with the password field. This is the default approach taken in the {@code org.springframework.security.crypto.password} package.
     * 
     * @param saltSourceToSet
     *            to use when attempting to decode passwords via the <code>PasswordEncoder</code>
     */
    public void setSaltSource(final SaltSource saltSourceToSet) {
        saltSource = saltSourceToSet;
    }

    protected SaltSource getSaltSource() {
        return saltSource;
    }

}

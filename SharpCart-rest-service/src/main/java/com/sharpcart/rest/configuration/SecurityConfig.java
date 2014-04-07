package com.sharpcart.rest.configuration;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.security.web.authentication.www.DigestAuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.DigestAuthenticationFilter;

import com.sharpcart.rest.security.SharpCartPasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

  @Autowired
  private UserDetailsService userDetailsService;
  
  @Autowired
  private AuthenticationProvider authenticationProvider;
  
  @Autowired
  DataSource dataSource;
  
  /*
  @Autowired
  protected void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
	      
	  //auth.userDetailsService(userDetailsService);
	  auth.authenticationProvider(authenticationProvider);
  }
*/
  
  
  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http
    	.csrf().disable()
    	.authorizeRequests()
    	.antMatchers("/aggregators/user/register",
    				"/aggregators/user/login",
    				"/aggregators/groceryItems/unavailable").permitAll()
        .antMatchers("/aggregators/optimize","/aggregators/user/update","/aggregators/user/syncSharpList","/aggregators/store/servingZIPCode").authenticated()
        .anyRequest().authenticated()
        .and()
        .httpBasic().realmName("SharpCart Security")
        .and()
        .sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
  }
  
	/* (non-Javadoc)
	 * @see org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter#configure(org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder)
	 */
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth
	    .jdbcAuthentication()
	      .dataSource(dataSource)
	      .usersByUsernameQuery(
	        "select userName, password, true " +
	        "from SharpCartUser where userName=?")
	      .authoritiesByUsernameQuery(
	        "select userName, 'ROLE_USER' from SharpCartUser where userName=?")
	      .passwordEncoder(new SharpCartPasswordEncoder());
	}
}

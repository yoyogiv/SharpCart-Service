package com.sharpcart.rest.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;

@EnableWebSecurity
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

  @Autowired
  private UserDetailsService userDetailsService;
  
  @Override
  protected void registerAuthentication(AuthenticationManagerBuilder auth) throws Exception {
	  auth.userDetailsService(this.userDetailsService);
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
	
	//only authorized users can optimize a list
    http.authorizeUrls()
        .antMatchers("/aggregators/optimize").hasRole("USER")
        .anyRequest().anonymous()
        .and()
        .httpBasic();
    
	//only authorized users can update a user profile
    http.authorizeUrls()
        .antMatchers("/aggregators/user/update").hasRole("USER")
        .anyRequest().anonymous()
        .and()
        .httpBasic();
    
    //allow anyone to register
    http.authorizeUrls()
	    .antMatchers("/aggregators/user/register").anonymous();
    
    //allow anyone to try and login
    http.authorizeUrls()
    .antMatchers("/aggregators/user/login").anonymous();
  }
}

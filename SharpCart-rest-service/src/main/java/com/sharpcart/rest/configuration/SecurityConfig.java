package com.sharpcart.rest.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

  @Autowired
  private UserDetailsService userDetailsService;
  
  @Autowired
  private AuthenticationProvider authenticationProvider;
  
  @Autowired
  protected void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
	      
	  //auth.userDetailsService(userDetailsService);
	  auth.authenticationProvider(authenticationProvider);
  }

  
  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http
    	.csrf().disable()
    	.authorizeRequests()
    	.antMatchers("/aggregators/user/register","/aggregators/user/login","/aggregators/groceryItems/unavailable").permitAll()
        .antMatchers("/aggregators/optimize","/aggregators/user/update").permitAll()
        .anyRequest().authenticated()
        //.and()
       // .httpBasic().realmName("SharpCart Security")
        .and()
        .sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
  }

  
}

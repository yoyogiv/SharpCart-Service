package com.sharpcart.rest.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@EnableWebSecurity
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

  @Override
  protected void registerAuthentication(AuthenticationManagerBuilder auth) throws Exception {
    auth.inMemoryAuthentication()
        .withUser("yoram.givon@gmail.com").password("Faeyy3303!").roles("USER");
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
	
	//only authorized users can optimize a list
    http.authorizeUrls()
        .antMatchers("/aggregators/optimize").hasRole("USER")
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

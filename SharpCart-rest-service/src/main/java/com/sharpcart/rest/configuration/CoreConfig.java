package com.sharpcart.rest.configuration;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

import com.sharpcart.rest.security.sharpCartAuthenticationProvider;
import com.sharpcart.rest.service.sharpCartUserDetailsService;

@Configuration
public class CoreConfig {

    @Bean
    public RequestMappingHandlerAdapter  annotationMethodHandlerAdapter()
    {
        final RequestMappingHandlerAdapter annotationMethodHandlerAdapter = new RequestMappingHandlerAdapter();
        final MappingJackson2HttpMessageConverter mappingJacksonHttpMessageConverter = new MappingJackson2HttpMessageConverter();

        final List<HttpMessageConverter<?>> httpMessageConverter = new ArrayList<HttpMessageConverter<?>>();
        httpMessageConverter.add(mappingJacksonHttpMessageConverter);

        final String[] supportedHttpMethods = { "POST", "GET", "HEAD" };

        annotationMethodHandlerAdapter.setMessageConverters(httpMessageConverter);
        annotationMethodHandlerAdapter.setSupportedMethods(supportedHttpMethods);

        return annotationMethodHandlerAdapter;
    }
    
    @Bean
    public UserDetailsService createUserDetailsService() {
      return new sharpCartUserDetailsService();
    }

    @Bean
    public AuthenticationProvider createAuthenticationProvider() {
      return new sharpCartAuthenticationProvider();
    }
}

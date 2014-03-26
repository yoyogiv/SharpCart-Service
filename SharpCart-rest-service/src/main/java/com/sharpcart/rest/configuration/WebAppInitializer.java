package com.sharpcart.rest.configuration;

import java.util.Set;

import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import javax.servlet.ServletRegistration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.filter.DelegatingFilterProxy;
import org.springframework.web.servlet.DispatcherServlet;

public class WebAppInitializer implements WebApplicationInitializer {

  private static Logger LOG = LoggerFactory.getLogger(WebAppInitializer.class);

  // {!begin onStartup}
  @Override
  public void onStartup(ServletContext servletContext) {
    final WebApplicationContext rootContext = createRootContext(servletContext);

    configureSpringSecurity(servletContext, rootContext);
    configureSpringMvc(servletContext, rootContext);
  }
  // {!end onStartup}

  // {!begin addToRootContext}
  private WebApplicationContext createRootContext(ServletContext servletContext) {
    final AnnotationConfigWebApplicationContext rootContext = new AnnotationConfigWebApplicationContext();
    rootContext.register(CoreConfig.class,SecurityConfig.class);
    rootContext.refresh();

    servletContext.addListener(new ContextLoaderListener(rootContext));
    servletContext.setInitParameter("defaultHtmlEscape", "true");

    return rootContext;
  }
  // {!end addToRootContext}

  private void configureSpringMvc(ServletContext servletContext, WebApplicationContext rootContext) {

    final AnnotationConfigWebApplicationContext mvcContext = new AnnotationConfigWebApplicationContext();
    mvcContext.register(MVCConfig.class);

    mvcContext.setParent(rootContext);

    final ServletRegistration.Dynamic appServlet = servletContext.addServlet(
        "webservice", new DispatcherServlet(mvcContext));
    appServlet.setLoadOnStartup(1);
    final Set<String> mappingConflicts = appServlet.addMapping("/*");

    if (!mappingConflicts.isEmpty()) {
      for (final String s : mappingConflicts) {
        LOG.error("Mapping conflict: " + s);
      }
      throw new IllegalStateException(
          "'webservice' cannot be mapped to '/'");
    }
  }

  // {!begin configureSpringSecurity}
  private void configureSpringSecurity(ServletContext servletContext, WebApplicationContext rootContext) {
    final FilterRegistration.Dynamic springSecurity = servletContext.addFilter("springSecurityFilterChain",
        new DelegatingFilterProxy("springSecurityFilterChain", rootContext));
    
    springSecurity.addMappingForUrlPatterns(null, true, "/*");
  }
  // {!end configureSpringSecurity}
}

package edu.ucar.unidata.cloudcontrol.config;

import java.util.Properties;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

@Configuration
@ImportResource("classpath:applicationContext-test.xml")
public class WebAppContext {

    @Bean
    public SimpleMappingExceptionResolver exceptionResolver() {
        SimpleMappingExceptionResolver exceptionResolver = new SimpleMappingExceptionResolver();

        Properties exceptionMappings = new Properties();
        exceptionMappings.put("org.springframework.security.access.AccessDeniedException", "denied");
        exceptionMappings.put("org.springframework.dao.DataRetrievalFailureException", "fatalError");
        exceptionMappings.put("com.github.dockerjava.api.exception.ConflictException", "fatalError");
        exceptionMappings.put("com.github.dockerjava.api.exception.NotFoundException", "fatalError");
        exceptionMappings.put("com.github.dockerjava.api.exception.NotModifiedException", "fatalError");

        exceptionResolver.setExceptionMappings(exceptionMappings);
        exceptionResolver.setDefaultErrorView("error"); 
        return exceptionResolver;
    }

    @Bean
    public ViewResolver jspViewResolver() {
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
 
        viewResolver.setViewClass(JstlView.class);
        viewResolver.setPrefix("/WEB-INF/views/");
        viewResolver.setSuffix(".jsp");
 
        return viewResolver;
    }
}

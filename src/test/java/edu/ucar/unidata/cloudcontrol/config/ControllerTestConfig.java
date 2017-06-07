package edu.ucar.unidata.cloudcontrol.config;

import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;


/**
 * Provides static factory methods that are used to create the Spring MVC 
 * infrastructure components for writing unit tests of Spring MVC controllers.
 */
public final class ControllerTestConfig {

    private static final String VIEW_BASE_PATH = "/WEB-INF/views/";
    private static final String VIEW_FILENAME_SUFFIX = ".jsp";

    /**
     * Prevents instantiation.
     */
    private ControllerTestConfig() {}

    public static ViewResolver jspViewResolver() {
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();

        viewResolver.setViewClass(JstlView.class);
        viewResolver.setPrefix(VIEW_BASE_PATH);
        viewResolver.setSuffix(VIEW_FILENAME_SUFFIX);

        return viewResolver;
    }
}

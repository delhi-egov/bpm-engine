package in.gov.bpm.app.config

import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer

/**
 * Created by user-1 on 24/6/16.
 */
class WebAppInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

    @Override
    protected Class<?>[] getRootConfigClasses() {
        return [ApplicationConfig];
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        return [DispatcherServletConfiguration];
    }

    @Override
    protected String[] getServletMappings() {
        return ['/'];
    }
}

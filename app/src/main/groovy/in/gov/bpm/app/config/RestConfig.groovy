package in.gov.bpm.app.config

import in.gov.bpm.app.impl.MapRestVariableConverter
import org.activiti.rest.common.application.ContentTypeResolver
import org.activiti.rest.common.application.DefaultContentTypeResolver
import org.activiti.rest.service.api.RestResponseFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration

/**
 * Created by user-1 on 24/6/16.
 */
@Configuration
class RestConfig {

    @Bean()
    public RestResponseFactory restResponseFactory() {
        RestResponseFactory restResponseFactory = new RestResponseFactory();
        restResponseFactory.getVariableConverters().add(mapRestVariableConverter());
        return restResponseFactory;
    }

    @Bean()
    public ContentTypeResolver contentTypeResolver() {
        ContentTypeResolver resolver = new DefaultContentTypeResolver();
        return resolver;
    }

    @Bean()
    public MapRestVariableConverter mapRestVariableConverter() {
        return new MapRestVariableConverter();
    }
}


package in.gov.bpm.app.config

import com.fasterxml.jackson.databind.ObjectMapper
import in.gov.bpm.engine.api.ActivitiService
import org.activiti.rest.service.api.PutAwareCommonsMultipartResolver
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.core.env.Environment
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.remoting.httpinvoker.HttpInvokerServiceExporter
import org.springframework.web.multipart.MultipartResolver
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor
import org.springframework.web.servlet.i18n.SessionLocaleResolver
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping
import in.gov.bpm.engine.config.ActivitiEngineConfiguration

/**
 * Created by user-1 on 24/6/16.
 */
@Configuration
@ComponentScan(["org.activiti.rest.exception", "org.activiti.rest.service.api", "in.gov.bpm.app.controller"])
@Import(value = [ActivitiEngineConfiguration])
class DispatcherServletConfiguration extends WebMvcConfigurationSupport {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private Environment environment;

    @Autowired
    private ActivitiService activitiService;

    @Bean
    public SessionLocaleResolver localeResolver() {
        return new SessionLocaleResolver();
    }

    @Bean
    public LocaleChangeInterceptor localeChangeInterceptor() {
        LocaleChangeInterceptor localeChangeInterceptor = new LocaleChangeInterceptor();
        localeChangeInterceptor.setParamName("language");
        return localeChangeInterceptor;
    }

    @Bean
    public MultipartResolver multipartResolver() {
        PutAwareCommonsMultipartResolver multipartResolver = new PutAwareCommonsMultipartResolver();
        return multipartResolver;
    }

    @Bean
    public RequestMappingHandlerMapping requestMappingHandlerMapping() {
        RequestMappingHandlerMapping requestMappingHandlerMapping = new RequestMappingHandlerMapping();
        requestMappingHandlerMapping.setUseSuffixPatternMatch(false);
        Object[] interceptors = [localeChangeInterceptor()].toArray();
        requestMappingHandlerMapping.setInterceptors(interceptors);
        return requestMappingHandlerMapping;
    }

    @Bean(name = "/activitiService")
    public HttpInvokerServiceExporter httpInvokerServiceExporter() {
        HttpInvokerServiceExporter serviceExporter = new HttpInvokerServiceExporter();
        serviceExporter.service = activitiService;
        serviceExporter.serviceInterface = ActivitiService;
        return serviceExporter;
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        addDefaultHttpMessageConverters(converters);
        for (HttpMessageConverter<?> converter: converters) {
            if (converter instanceof MappingJackson2HttpMessageConverter) {
                MappingJackson2HttpMessageConverter jackson2HttpMessageConverter = (MappingJackson2HttpMessageConverter) converter;
                jackson2HttpMessageConverter.setObjectMapper(objectMapper);
                break;
            }
        }
    }

    @Override
    protected void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
        configurer.favorPathExtension(false);
    }

}

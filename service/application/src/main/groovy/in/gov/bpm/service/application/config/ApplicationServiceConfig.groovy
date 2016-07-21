package in.gov.bpm.service.application.config

import in.gov.bpm.engine.api.ActivitiService
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import in.gov.bpm.db.config.DbConfig
import org.springframework.context.annotation.PropertySource
import org.springframework.context.annotation.PropertySources
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer
import org.springframework.remoting.httpinvoker.HttpInvokerProxyFactoryBean

/**
 * Created by vaibhav on 21/7/16.
 */
@Configuration
@ComponentScan(basePackages = 'in.gov.bpm.service.application')
@Import(value = [DbConfig])
@PropertySources(@PropertySource("file:/egov.properties"))
class ApplicationServiceConfig {

    @Value('${bpm.url}')
    String bpmUrl;

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertyConfig() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    @Bean
    public ActivitiService httpInvokerProxyFactoryBean() {
        HttpInvokerProxyFactoryBean httpInvokerProxyFactoryBean = new HttpInvokerProxyFactoryBean();
        httpInvokerProxyFactoryBean.serviceInterface = ActivitiService;
        httpInvokerProxyFactoryBean.serviceUrl = bpmUrl;
        return (ActivitiService) httpInvokerProxyFactoryBean.getObject();
    }
}

package in.gov.bpm.service.user.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import

import in.gov.bpm.db.config.DbConfig
import org.springframework.context.annotation.PropertySource
import org.springframework.context.annotation.PropertySources
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer
import org.springframework.web.client.RestTemplate

/**
 * Created by vaibhav on 20/7/16.
 */
@Configuration
@ComponentScan(basePackages = 'in.gov.bpm.service.user')
@Import(value = [DbConfig])
@PropertySources(@PropertySource("file:/egov.properties"))
class UserServiceConfig {

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertyConfig() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    @Bean
    RestTemplate restTemplate() {
        return new RestTemplate();
    }
}

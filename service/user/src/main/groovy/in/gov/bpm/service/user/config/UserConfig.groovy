package in.gov.bpm.service.user.config

import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import

import in.gov.bpm.db.config.DbConfig

/**
 * Created by vaibhav on 20/7/16.
 */
@Configuration
@ComponentScan(basePackages = 'in.gov.bpm.service.user')
@Import(value = [DbConfig])
class UserConfig {
}

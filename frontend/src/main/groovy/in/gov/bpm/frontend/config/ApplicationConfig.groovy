package in.gov.bpm.frontend.config

import in.gov.bpm.shared.config.ExceptionConfig
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import in.gov.bpm.service.user.config.UserServiceConfig
import in.gov.bpm.service.application.config.ApplicationServiceConfig

/**
 * Created by user-1 on 24/6/16.
 */
@Configuration
@Import(value = [ExceptionConfig, UserServiceConfig, ApplicationServiceConfig, SecurityConfiguration])
class ApplicationConfig {
}

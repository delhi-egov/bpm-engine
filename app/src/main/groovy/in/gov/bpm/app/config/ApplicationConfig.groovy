package in.gov.bpm.app.config

import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import

/**
 * Created by user-1 on 24/6/16.
 */
@Configuration
@ComponentScan(basePackages = ["in.gov.bpm.app"])
class ApplicationConfig {
}

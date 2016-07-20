package in.gov.bpm.shared.config

import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration

/**
 * Created by user-1 on 6/4/16.
 */
@Configuration
@ComponentScan(basePackages = 'in.gov.bpm.shared.exception')
class ExceptionConfig {
}

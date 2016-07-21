package in.gov.bpm.app.impl

import org.activiti.engine.IdentityService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component

import javax.servlet.Filter
import javax.servlet.FilterChain
import javax.servlet.FilterConfig
import javax.servlet.ServletException
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse

/**
 * Created by vaibhav on 21/7/16.
 */
@Component
class IdentityInjectionFilter implements Filter {

    @Autowired
    IdentityService identityService;

    @Override
    void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        String username = SecurityContextHolder.context.authentication.principal.toString();
        identityService.authenticatedUserId = username;
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    void destroy() {

    }
}

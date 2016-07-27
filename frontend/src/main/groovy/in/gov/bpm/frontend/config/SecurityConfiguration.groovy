package in.gov.bpm.frontend.config

import in.gov.bpm.frontend.impl.AppBasicAuthenticationProvider
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity
import org.springframework.security.config.http.SessionCreationPolicy

/**
 * Created by user-1 on 24/6/16.
 */
@Configuration
@EnableWebSecurity(debug = true)
@EnableWebMvcSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, jsr250Enabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        //auth.authenticationProvider(authenticationProvider());
    }

    @Bean(name="authenticationManager")
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        return new AppBasicAuthenticationProvider();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authenticationProvider(authenticationProvider())
                .csrf().disable()
                .authorizeRequests()
                .antMatchers('/user/register').permitAll()
                .antMatchers('/user/login', '/user/logout', '/user/me').hasAnyRole("ADMIN", "USER", "UNVERIFIED")
                .antMatchers("/swagger-ui.html").hasRole("ADMIN")
                .antMatchers("/webjars/**").hasRole("ADMIN")
                .antMatchers("/notification").hasRole("ADMIN")
                .antMatchers("/user/generateOtp").hasAnyRole("ADMIN", "UNVERIFIED")
                .antMatchers("/user/verifyOtp").hasAnyRole("ADMIN", "UNVERIFIED")
                .antMatchers('/**').hasAnyRole("ADMIN", "USER")
                .and()
                .httpBasic().and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED).maximumSessions(1);

    }

}

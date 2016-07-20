package in.gov.bpm.frontend.impl

import in.gov.bpm.db.entity.User
import in.gov.bpm.service.user.api.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority

/**
 * Created by vaibhav on 20/7/16.
 */
class AppBasicAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    UserService userService;

    @Override
    Authentication authenticate(Authentication authentication) throws AuthenticationException {
        Long phone = Long.valueOf(authentication.getName());
        String password = authentication.getCredentials().toString();

        User user = userService.authenticate(phone, password);
        if (user != null) {
            Collection<GrantedAuthority> grantedAuthorities = new ArrayList<GrantedAuthority>();
            grantedAuthorities.add(new SimpleGrantedAuthority(user.role));
            UserDetails userDetails = new UserDetails(user, grantedAuthorities);
            userDetails.setAuthenticated(true);
            return userDetails;
        } else {
            throw new BadCredentialsException("Authentication failed for this phone and password");
        }
    }

    @Override
    boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}

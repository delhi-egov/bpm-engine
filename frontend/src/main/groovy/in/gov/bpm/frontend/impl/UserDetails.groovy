package in.gov.bpm.frontend.impl

import in.gov.bpm.db.entity.User
import org.springframework.security.authentication.AbstractAuthenticationToken
import org.springframework.security.core.GrantedAuthority

/**
 * Created by vaibhav on 20/7/16.
 */
class UserDetails extends AbstractAuthenticationToken {

    User user;

    UserDetails(User user, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.user = user;
    }

    @Override
    String getCredentials() {
        return user.password;
    }

    @Override
    Long getPrincipal() {
        return user.phone;
    }
}

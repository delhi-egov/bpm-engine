package in.gov.bpm.app.controller

import in.gov.bpm.app.pojo.User
import in.gov.bpm.app.service.UserService
import org.activiti.engine.IdentityService
import org.activiti.engine.impl.persistence.entity.UserEntity
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler
import org.springframework.security.web.bind.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import java.util.stream.Collectors

/**
 * Created by vaibhav on 20/7/16.
 */
@RestController
@RequestMapping('/user')
class UserController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    IdentityService identityService;


    @RequestMapping(value = '/login', method = RequestMethod.POST)
    User login(@AuthenticationPrincipal UsernamePasswordAuthenticationToken userDetails) {
        return getApiUserForAuthToken(userDetails);
    }

    @RequestMapping(value = '/logout', method = RequestMethod.POST)
    Boolean logout(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return true;
    }

    @RequestMapping(value = '/me', method = RequestMethod.GET)
    User me(@AuthenticationPrincipal UsernamePasswordAuthenticationToken userDetails) {
        return getApiUserForAuthToken(userDetails);
    }

    private User getApiUserForAuthToken(UsernamePasswordAuthenticationToken userDetails) {
        org.activiti.engine.identity.User user = identityService.createUserQuery().userId(userDetails.principal.toString()).singleResult();
        User apiUser = new User(
                firstName: user.firstName,
                lastName: user.lastName,
                username: user.id,
                email: user.email
        );

        List<String> groups = identityService.createGroupQuery().groupMember(user.id).list().stream().map({group -> group.id}).collect(Collectors.toList());
        apiUser.groups = groups;
        return apiUser;
    }
}

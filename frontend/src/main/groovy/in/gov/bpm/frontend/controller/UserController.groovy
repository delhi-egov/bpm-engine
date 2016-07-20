package in.gov.bpm.frontend.controller

import in.gov.bpm.db.entity.User
import in.gov.bpm.frontend.impl.UserDetails
import in.gov.bpm.service.user.api.UserService
import in.gov.bpm.shared.exception.MissingPropertiesException
import org.apache.commons.lang3.StringUtils
import org.codehaus.groovy.util.StringUtil
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.AuthenticationManager
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

/**
 * Created by vaibhav on 20/7/16.
 */
@RestController
@RequestMapping('/user')
class UserController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserService userService;

    @RequestMapping(value = '/register', method = RequestMethod.POST)
    User register(@RequestBody User user) {
        if(user.phone == null || user.password == null || StringUtils.isAnyEmpty(user.phone.toString(), user.password)) {
            throw new MissingPropertiesException(['phone', 'password']);
        }
        return userService.register(user);
    }

    @RequestMapping(value = '/login', method = RequestMethod.POST)
    User login(@AuthenticationPrincipal UserDetails userDetails) {
        return userService.findByPhone(userDetails.getPrincipal());
    }

    @RequestMapping(value = '/logout', method = RequestMethod.POST)
    Boolean logout(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return true;
    }
}

package in.gov.bpm.app.service

import org.activiti.engine.IdentityService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

/**
 * Created by vaibhav on 31/7/16.
 */
@Component
class UserService {

    public static final String superior = 'SUPERIOR';

    @Autowired
    IdentityService identityService;

    void addSuperiorToUser(String userId, String superiorId) {
        identityService.setUserInfo(userId, superior, superiorId);
    }

    void removeSuperiorForUser(String userId) {
        identityService.deleteUserInfo(userId, superior);
    }

    String getSuperiorForUser(String userId) {
        return identityService.getUserInfo(userId, superior);
    }
}

package in.gov.bpm.service.user.impl

import in.gov.bpm.db.entity.User
import in.gov.bpm.db.repository.UserRepository
import in.gov.bpm.service.user.api.UserService
import in.gov.bpm.shared.exception.AccountExistsException
import in.gov.bpm.shared.exception.InvalidValueException
import org.mindrot.jbcrypt.BCrypt
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

import javax.persistence.EntityManager

/**
 * Created by vaibhav on 20/7/16.
 */
@Component
class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    EntityManager entityManager;

    @Override
    User findById(Long id) {
        User user = userRepository.findOne(id);
        if(user != null) {
            entityManager.detach(user);
        }
        return user;
    }

    @Override
    User findByPhone(Long phone) {
        User user = userRepository.findByPhone(phone);
        return detachAndReturn(user);
    }

    @Override
    User authenticate(Long phone, String password) {
        User user = findByPhone(phone);
        if(user == null) {
            return null;
        }
        else {
            if (BCrypt.checkpw(password, user.password)) {
                return detachAndReturn(user);
            }
            else {
                return null;
            }
        }
    }

    @Override
    User save(User user) {
        user =  userRepository.save(user);
        return detachAndReturn(user);
    }

    @Override
    User register(User user) {
        User existingUser = userRepository.findByPhone(user.phone);
        if(existingUser != null) {
            throw new AccountExistsException(user.phone.toString());
        }
        user.password = BCrypt.hashpw(user.password, BCrypt.gensalt());
        user.role = 'USER';
        user = userRepository.save(user);
        return detachAndReturn(user);
    }

    private User detachAndReturn(User user) {
        if(user != null) {
            entityManager.detach(user);
        }
        return user;
    }
}

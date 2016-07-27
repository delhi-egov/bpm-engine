package in.gov.bpm.service.user.impl

import groovy.util.logging.Slf4j
import in.gov.bpm.db.entity.Otp
import in.gov.bpm.db.entity.User
import in.gov.bpm.db.repository.OtpRepository
import in.gov.bpm.db.repository.UserRepository
import in.gov.bpm.service.user.api.UserService
import in.gov.bpm.shared.exception.AccountExistsException
import in.gov.bpm.shared.exception.InvalidStateException
import in.gov.bpm.shared.exception.InvalidValueException
import org.mindrot.jbcrypt.BCrypt
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate

import javax.persistence.EntityManager

/**
 * Created by vaibhav on 20/7/16.
 */
@Component
@Slf4j
class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    EntityManager entityManager;

    @Autowired
    OtpRepository otpRepository;

    @Autowired
    RestTemplate restTemplate;

    @Value('${dev}')
    Boolean devMode;

    @Value('${sms.url}')
    String smsUrl;

    @Value('${sms.username}')
    String smsUsername;

    @Value('${sms.password}')
    String smsPassword;

    @Value('${sms.senderId}')
    String smsSenderId;

    @Value('${sms.message}')
    String message;

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
        user.role = 'UNVERIFIED';
        user = userRepository.save(user);
        return detachAndReturn(user);
    }

    @Override
    void generateOtp(User user) {
        Otp otp = otpRepository.findByUser_Id(user.id);
        if(otp == null) {
            otp = new Otp(
                    otpCount: 0,
                    user: user
            );
        }
        else {
            entityManager.detach(otp);
        }
        otp.attemptCount = 0;
        otp.otpCount = otp.otpCount + 1;
        otp.otp = generateRandomOtp();
        otpRepository.save(otp);
        if(!devMode) {
            sendOtpToUser(user, otp.otp);
        }
        else {
            log.info("The otp is " + otp.otp);
        }
    }

    @Override
    Boolean verifyOtp(User user, Integer otpProvided) {
        Otp otp = otpRepository.findByUser_Id(user.id);
        if(otp == null) {
            throw new InvalidStateException("Otp has not been generated for user");
        }
        otp.attemptCount = otp.attemptCount + 1;
        otpRepository.save(otp);
        if(otp.otp == otpProvided) {
            user.role = "USER";
            userRepository.save(user);
            return true;
        }
        return false;
    }

    private Integer generateRandomOtp() {
        if(devMode) {
            return 123456;
        }
        Random random = new Random();
        return 100000 + random.nextInt(900000);
    }

    private void sendOtpToUser(User user, Integer otp) {
        String url = smsUrl + '?username=' + smsUsername + '&pin=' + smsPassword + '&message={message}&signature=' + smsSenderId + '&mnumber={phone}';
        restTemplate.getForObject(url, String, otp + " " + message, user.phone);
    }

    private User detachAndReturn(User user) {
        if(user != null) {
            entityManager.detach(user);
        }
        return user;
    }
}

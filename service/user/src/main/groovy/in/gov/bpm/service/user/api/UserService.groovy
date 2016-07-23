package in.gov.bpm.service.user.api

import in.gov.bpm.db.entity.User

/**
 * Created by vaibhav on 20/7/16.
 */
interface UserService {
    User findById(Long id);
    User findByPhone(Long phone);
    User authenticate(Long phone, String password);
    User save(User user);
    User register(User user);
    void generateOtp(User user);
    Boolean verifyOtp(User user, Integer otpProvided);
}

package in.gov.bpm.db.repository

import in.gov.bpm.db.entity.User
import org.springframework.data.repository.PagingAndSortingRepository

/**
 * Created by vaibhav on 20/7/16.
 */
interface UserRepository extends PagingAndSortingRepository<User, Long> {
    User findByPhone(Long phone);
}

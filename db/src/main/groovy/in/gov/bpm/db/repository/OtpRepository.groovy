package in.gov.bpm.db.repository

import in.gov.bpm.db.entity.Otp
import org.springframework.data.repository.PagingAndSortingRepository

/**
 * Created by vaibhav on 23/7/16.
 */
interface OtpRepository extends PagingAndSortingRepository<Otp, Long> {
    Otp findByUser_Id(Long id);
}
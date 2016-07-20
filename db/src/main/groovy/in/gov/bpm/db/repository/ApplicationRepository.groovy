package in.gov.bpm.db.repository

import in.gov.bpm.db.entity.Application
import org.springframework.data.repository.PagingAndSortingRepository

/**
 * Created by vaibhav on 20/7/16.
 */
interface ApplicationRepository extends PagingAndSortingRepository<Application, Long> {
    List<Application> findByUser_Id(Long id);
}
package in.gov.bpm.db.repository

import in.gov.bpm.db.entity.ApplicationType
import org.springframework.data.repository.PagingAndSortingRepository

/**
 * Created by vaibhav on 23/7/16.
 */
interface ApplicationTypeRepository extends PagingAndSortingRepository<ApplicationType, Long> {

    ApplicationType findByName(String name);
}
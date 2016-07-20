package in.gov.bpm.db.repository

import in.gov.bpm.db.entity.Form
import org.springframework.data.repository.PagingAndSortingRepository

/**
 * Created by vaibhav on 20/7/16.
 */
interface FormRepository extends PagingAndSortingRepository<Form, Long> {
    List<Form> findByApplication_Id(Long applicationId);
}
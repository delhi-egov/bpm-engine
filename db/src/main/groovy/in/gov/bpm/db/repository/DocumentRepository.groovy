package in.gov.bpm.db.repository

import in.gov.bpm.db.entity.Document
import org.springframework.data.repository.PagingAndSortingRepository

/**
 * Created by vaibhav on 20/7/16.
 */
interface DocumentRepository extends PagingAndSortingRepository<Document, Long> {
    List<Document> findByApplication_Id(Long applicationId);
}

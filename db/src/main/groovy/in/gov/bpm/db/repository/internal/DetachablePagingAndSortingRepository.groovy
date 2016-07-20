package in.gov.bpm.db.repository.internal

import org.springframework.data.repository.NoRepositoryBean
import org.springframework.data.repository.PagingAndSortingRepository

@NoRepositoryBean
interface DetachablePagingAndSortingRepository<T, ID extends Serializable> extends PagingAndSortingRepository<T, ID>{
    void detach(T entity)
    void detach(Collection<T> entities)
}
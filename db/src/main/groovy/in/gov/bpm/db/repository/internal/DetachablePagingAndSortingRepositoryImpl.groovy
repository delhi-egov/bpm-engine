package in.gov.bpm.db.repository.internal

import org.springframework.data.jpa.repository.support.JpaEntityInformation
import org.springframework.data.jpa.repository.support.SimpleJpaRepository

import javax.persistence.EntityManager

class DetachablePagingAndSortingRepositoryImpl<T, ID extends Serializable> extends SimpleJpaRepository<T, ID> implements DetachablePagingAndSortingRepository<T,ID> {

    private EntityManager entityManager;

    DetachablePagingAndSortingRepositoryImpl(JpaEntityInformation<T, ID> entityInformation, EntityManager entityManager) {
        super(entityInformation, entityManager)
        this.entityManager = entityManager
    }

    @Override
    void detach(T entity) {
        if (entity != null)
            entityManager.detach(entity)
    }

    @Override
    void detach(Collection<T> entities) {
        if (entities != null)
            entities.each {
                detach(it)
            }
    }
}

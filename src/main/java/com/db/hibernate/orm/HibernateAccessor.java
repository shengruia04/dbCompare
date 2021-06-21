package com.db.hibernate.orm;

import com.db.hibernate.entity.AbstractEntity;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

@Component
public class HibernateAccessor {

    @PersistenceContext
    private EntityManager entityManager;

    public <T> T load(Object id, Class<? extends AbstractEntity> entityClass) {
        AbstractEntity t = entityManager.find(entityClass, id);
        if(t != null){
            t.doDeserialize();
        }
        return (T) t;
    }

    @Transactional(rollbackOn = Exception.class)
    public void save(AbstractEntity entity) {
        entity.doSerialize();
        entityManager.merge(entity);
        entityManager.flush();
    }
}

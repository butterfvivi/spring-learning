package org.vivi.framework.lucky.mongodb.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import org.vivi.framework.lucky.mongodb.entity.WorkBookEntity;

@Repository
public interface WorkBookRepository extends MongoRepository<WorkBookEntity,String> {
}

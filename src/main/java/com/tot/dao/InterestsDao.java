package com.tot.dao;

import com.tot.pojo.Interests;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * @author karthik on 15/01/22.
 * @project totservices
 */

@Repository public interface InterestsDao extends MongoRepository<Interests, String> {
    Interests findByUserId(String userId);
}

package com.tot.dao;

import com.tot.pojo.DeletedPost;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * @author karthik on 11/07/22.
 * @project totservices
 */

@Repository public interface DeletedPostDao extends MongoRepository<DeletedPost, String> {

}

package com.tot.dao;

import com.tot.pojo.Comment;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author karthik on 06/12/21.
 * @project totservices
 */


@Repository public interface CommentDao extends MongoRepository<Comment, String> {

    List<Comment> findByPostId(String postId, Sort sort, PageRequest page);
}

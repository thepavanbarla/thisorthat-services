package com.tot.dao;

import com.tot.pojo.UserVote;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author karthik on 20/01/22.
 * @project totservices
 */

@Repository public interface VoteDao extends MongoRepository<UserVote, String> {


    List<UserVote> findByPostId(String postId, Pageable pageable);

    List<UserVote> findByUserId(String userId, Pageable pageable);

    List<UserVote> findByUserIdAndPostId(String userId, String postId, Pageable pageable);

    List<UserVote> findByOptionId(String optionId);

    List<UserVote> findByOptionIdAndUserDetailsGenderIgnoreCase(String optionId, String gender);

    List<UserVote> findByPostIdAndUserDetailsGenderIgnoreCase(String postId, String gender);
}

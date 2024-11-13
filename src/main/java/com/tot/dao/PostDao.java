package com.tot.dao;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.tot.pojo.Post;

/**
 * @author karthik on 04/12/21.
 * @project totservices
 */

@Repository public interface PostDao extends MongoRepository<Post, String> {

    List<Post> findByCategoriesInAndIsPrivate(List<String> categories, Boolean isPrivate, Pageable pageable);

    List<Post> findByUserIdIn(List<String> followingUserIds, PageRequest pageable);

    @Query(value = "{$and : [{ 'createdTime' : { $gt: {$date: ?1} }}, { 'isPrivate' : false }, {'userId' : {$in: ?0}}]}", fields = " { tot_post : 0 }")
    List<Post> findPostsByUserIdIn(List<String> followingUserIds, String lastFeedTime, Sort sort,
        PageRequest page);

    @Query(value = "{$and : [{ 'createdTime' : { $gt: {$date: ?0} }}, { 'isPrivate' : false }, {'userId' : { $nin : ?1}}]}", fields = " { tot_post : 0 }")
    List<Post> findPublicFeed(String lastFeedTime, List<String> followingUserIds, Sort sort, PageRequest page);
    
    @Query(value = "{$and : [{ 'createdTime' : { $gt: {$date: ?0} }}, { 'isPrivate' : false }, {'userId' : { $nin : ?1}}, {categories: ?2}]}", fields = " { tot_post : 0 }")
    List<Post> findPublicFeedForInterest(String lastFeedTime, List<String> followingUserIds, String interest, Sort sort, PageRequest page);

    List<Post> findByUserId(String userId, PageRequest page, Sort sort);
    
    @Query(value="{tags: ?0}")
    List<Post> findByTagsContaining(String tag, PageRequest page, Sort sort);
    
    List<Post> findByDraftPostId(String draftPostId, PageRequest page, Sort sort);

}

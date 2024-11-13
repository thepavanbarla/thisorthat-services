package com.tot.service;

import com.tot.pojo.Comment;
import com.tot.pojo.Post;
import com.tot.pojo.RestResponse;

/**
 * @author karthik on 17/08/21.
 * @project tot-services
 */


public interface PostService {

    RestResponse createPost(Post post);

    RestResponse getPost(String postId, boolean isPublic);

    Post getPostImages(String postId);

    RestResponse getPostsByCategories(String[] categories, int skip, int limit);
    
    RestResponse getPostsByTag(String tag, int skip, int limit);

    RestResponse updatePost(Post post);

    RestResponse votePost(String userId, String postId, Integer choice);

    RestResponse sharePost(String postId, String userId);

    RestResponse addComment(Comment comment);

    RestResponse deleteComment(String commentId);

    RestResponse getComments(String postId, int skip, int limit);

    RestResponse getFeed(String lastFeedTime, int skip, int limit);

    RestResponse getPublicFeed(String lastFeedTime, int skip, int limit, String interest);

    RestResponse getPostByUserId(String userId, int skip, int limit, boolean isPublic);

    RestResponse deletePost(String postId);
    
    RestResponse getTags(String tag);
    
    RestResponse getCollabPosts(String draftPostId, int skip, int limit);
}

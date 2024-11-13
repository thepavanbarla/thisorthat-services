package com.tot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.tot.pojo.Comment;
import com.tot.pojo.Interests;
import com.tot.pojo.ListSearchWrapper;
import com.tot.pojo.Post;
import com.tot.pojo.RestResponse;
import com.tot.service.InterestService;
import com.tot.service.MediaManagerService;
import com.tot.service.PostService;
import com.tot.service.PostStatsService;
import com.tot.utils.Constants;

import lombok.extern.slf4j.Slf4j;
import java.util.concurrent.CompletableFuture;

/**
 * @author karthik on 17/08/21.
 * @project tot-services
 */


@Slf4j 
@RestController 
public class PostController {

    @Autowired PostService postService;

    @Autowired InterestService interestService;
    @Autowired MediaManagerService mediaManagerService;
    @Autowired PostStatsService postStatsService;

    @PutMapping("/post/create")
    public ResponseEntity<RestResponse> createPost(@RequestBody Post post) {
        RestResponse response = postService.createPost(post);
        log.info("Post user id is {}", post.getUserId());
        CompletableFuture.runAsync(() -> {
            mergeImageUtil(post);
        });
        return ResponseEntity.ok(response);
    }

    @GetMapping("/post/{postId}")
    public ResponseEntity<RestResponse> getPost(@PathVariable String postId) {
        RestResponse response = postService.getPost(postId, false);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/post/delete/{postId}")
    public ResponseEntity<RestResponse> deletePost(@PathVariable String postId) {
        RestResponse response = postService.deletePost(postId);
        return ResponseEntity.ok(response);
    }


    @GetMapping({"/post/user/{userId}"})
    public ResponseEntity<RestResponse> getPostByUserId(@PathVariable String userId,
    		@RequestParam(required = false) Integer skip,
        @RequestParam(required = false) Integer limit) {
        if (skip == null || limit == null) {
            skip = Constants.DEFAULT_SKIP;
            limit = Constants.DEFAULT_LIMIT;
        }
        
        RestResponse response = postService.getPostByUserId(userId, skip, limit, false);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping({"/public/post/user/{userId}"})
    public ResponseEntity<RestResponse> getPostByPublicUserId(@PathVariable String userId) {
        RestResponse response = postService.getPostByUserId(userId, 0, 3, true);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/post/search/categories")
    public ResponseEntity<RestResponse> getPostsByCategories(
        @RequestBody ListSearchWrapper wrapper) {
        log.info("wrapper content: " + wrapper.getListItems());
        log.info("wrapper skip: " + wrapper.getSkip());
        log.info("wrapper limit: " + wrapper.getLimit());
        RestResponse response = postService
            .getPostsByCategories(wrapper.getListItems(), wrapper.getSkip(), wrapper.getLimit());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/public/post/search/categories")
    public ResponseEntity<RestResponse> getPostsByCategoriesPublic(
        @RequestBody ListSearchWrapper wrapper) {
        RestResponse response = postService
            .getPostsByCategories(wrapper.getListItems(), wrapper.getSkip(), wrapper.getLimit());
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/post/search/tags")
    public ResponseEntity<RestResponse> getPostsByCategories(@RequestParam String tag, @RequestParam Integer skip, @RequestParam Integer limit) {
        RestResponse response = postService.getPostsByTag(tag, skip, limit);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/tags/search/{tag}")
    public ResponseEntity<RestResponse> getTagsAutocompelte(@PathVariable String tag) {
        RestResponse response = postService.getTags(tag);
        return ResponseEntity.ok(response);
    }

    @GetMapping({"/feed", "/feed/{lastFeedTime}"})
    public ResponseEntity<RestResponse> getFeed(@PathVariable(required = false) String lastFeedTime,
        @RequestParam(required = false) Integer skip,
        @RequestParam(required = false) Integer limit) {
        if (lastFeedTime == null) {
            lastFeedTime = "2021-01-01T00:00:00.000Z";
        }
        if (skip == null || limit == null) {
            skip = Constants.DEFAULT_SKIP;
            limit = Constants.DEFAULT_LIMIT;
        }
        
        RestResponse response = postService.getFeed(lastFeedTime, skip, limit);
        return ResponseEntity.ok(response);
    }

    @GetMapping({"/feed/public", "/feed/public/{lastFeedTime}"})
    public ResponseEntity<RestResponse> getPublicFeed(
        @PathVariable(required = false) String lastFeedTime,
        @RequestParam(required = false) Integer skip,
        @RequestParam(required = false) Integer limit,
        @RequestParam(required = false) String interest) {
        if (lastFeedTime == null) {
            lastFeedTime = "2021-01-01T00:00:00.000Z";
        }
        if (skip == null || limit == null) {
            skip = Constants.DEFAULT_SKIP;
            limit = Constants.DEFAULT_LIMIT;
        }
        RestResponse response = postService.getPublicFeed(lastFeedTime, skip, limit, interest);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/comment/add")
    public ResponseEntity<RestResponse> addComment(@RequestBody Comment comment) {
        RestResponse response = postService.addComment(comment);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/comment/delete/{commentId}")
    public ResponseEntity<RestResponse> deleteComment(@PathVariable String commentId) {
        RestResponse response = postService.deleteComment(commentId);
        return ResponseEntity.ok(response);
    }

    @GetMapping({"/comments/{postId}"})
    public ResponseEntity<RestResponse> getComments(@PathVariable String postId,
		@RequestParam(required = false) Integer skip,
        @RequestParam(required = false) Integer limit) {
        if (skip == null || limit == null) {
            skip = Constants.DEFAULT_SKIP;
            limit = Constants.DEFAULT_LIMIT;
        }
        RestResponse response = postService.getComments(postId, skip, limit);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/interests/save")
    public ResponseEntity<RestResponse> storeInterests(@RequestBody Interests interests) {
        RestResponse response = interestService.saveInterests(interests);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/interests")
    public ResponseEntity<RestResponse> getInterestsByUserId() {
        RestResponse response = interestService.getInterests();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/post/stats/{postId}")
    public ResponseEntity<RestResponse> getPostStats(@PathVariable String postId) {
        RestResponse response = postStatsService.getPostStatsByPostId(postId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/post/upload_picture")
    public ResponseEntity<RestResponse> uploadFile(@RequestParam MultipartFile file) {
        RestResponse accountsResponse = mediaManagerService.uploadPostPicture(file);
        return ResponseEntity.ok(accountsResponse);
    }

    @GetMapping("/public/post/{postId}")
    public ResponseEntity<RestResponse> getPublicPost(@PathVariable String postId) {
        RestResponse response = postService.getPost(postId, true);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/fetchCollab/all/{draftPostId}")
    public ResponseEntity<RestResponse> getCollabPostsForDraft(@PathVariable String draftPostId,
    		@RequestParam(required = false) Integer skip,
            @RequestParam(required = false) Integer limit) {
    	if (skip == null || limit == null) {
            skip = Constants.DEFAULT_SKIP;
            limit = Constants.DEFAULT_LIMIT;
        }
        RestResponse response = postService.getCollabPosts(draftPostId, skip, limit);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/public/merged/{postId}")
    public ResponseEntity<RestResponse> getMergedImage(@PathVariable String postId) {
        Post post = postService.getPostImages(postId);
        RestResponse merged = mergeImageUtil(post);
        return ResponseEntity.ok(merged);
    }

    private RestResponse mergeImageUtil(Post post) {
        RestResponse merged = RestResponse.getFailedResponse(400, "Failed to merge images");
        if (post != null) {
            try {
                String img1 = post.getOptions().get(0).getPicture();
                img1 = img1.replaceFirst("post-pictures", "post-pictures/medium");
                String img2 = post.getOptions().get(1).getPicture();
                img2 = img2.replaceFirst("post-pictures", "post-pictures/medium");

                merged = mediaManagerService
                    .mergePostImages(img1, img2, post.getPostId(), post.getUserId(), false);
                
                String img3 = post.getOptions().get(0).getPicture();
                img1 = img3.replaceFirst("post-pictures", "post-pictures/small");
                String img4 = post.getOptions().get(1).getPicture();
                img2 = img4.replaceFirst("post-pictures", "post-pictures/small");

                mediaManagerService.mergePostImages(img1, img2, post.getPostId(), post.getUserId(), true);
            } catch (Exception e) {
                e.printStackTrace();
                merged = RestResponse
                    .getFailedResponse(400, "Failed to merge images: " + e.getMessage());
            }
        }
        return merged;
    }

}

package com.tot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.tot.pojo.RestResponse;
import com.tot.pojo.UserVote;
import com.tot.service.VoteService;
import com.tot.utils.Constants;

/**
 * @author karthik on 19/01/22.
 * @project totservices
 */

@RestController public class VoteController {

    @Autowired VoteService voteService;

    @PutMapping("/vote/save")
    public ResponseEntity<RestResponse> saveVote(@RequestBody UserVote vote) {
        RestResponse response = voteService.saveVote(vote);
        return ResponseEntity.ok(response);
    }

    @GetMapping({"/vote/post/{postId}", "/vote/post/{postId}/{skip}/{limit}"})
    public ResponseEntity<RestResponse> getVotesByPostId(@PathVariable String postId,
        @PathVariable(required = false) Integer skip,
        @PathVariable(required = false) Integer limit) {
        if (skip == null || limit == null) {
            skip = Constants.DEFAULT_SKIP;
            limit = Constants.DEFAULT_LIMIT;
        }
        RestResponse response = voteService.getVotesByPostId(postId, skip, limit);
        return ResponseEntity.ok(response);
    }

    @GetMapping({"/vote/user/{userId}", "/vote/user/{userId}/{skip}/{limit}"})
    public ResponseEntity<RestResponse> getVotesByUserId(@PathVariable String userId,
        @PathVariable(required = false) Integer skip,
        @PathVariable(required = false) Integer limit) {
        if (skip == null || limit == null) {
            skip = Constants.DEFAULT_SKIP;
            limit = Constants.DEFAULT_LIMIT;
        }
        RestResponse response = voteService.getVotesByUserId(userId, skip, limit);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/vote/user_post/{userId}/{postId}")
    public ResponseEntity<RestResponse> getVotesByUserId(@PathVariable String userId,
        @PathVariable String postId) {
        RestResponse response = voteService.getVotesByUserIdAndPostId(userId, postId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/vote/id/{optionId}")
    public ResponseEntity<RestResponse> getVotesByOptionId(@PathVariable String optionId) {
        RestResponse response = voteService.getVotesById(optionId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/vote/analyze/{optionId}/{gender}")
    public ResponseEntity<RestResponse> getVotesAnalysisByOptionIdAndGender(
        @PathVariable String optionId, @PathVariable String gender) {
        //TODO: option.id, userDetails.gender.. will have to call 4 times for each post for now
        //Eg: option1-male, option1-female, option2-male, option2-female
        RestResponse response = voteService.getVotesAnalysisByOptionIdAndGender(optionId, gender);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/vote/analyze_post/{postId}/{gender}")
    public ResponseEntity<RestResponse> getVotesAnalysisByPostIdAndGender(
        @PathVariable String postId, @PathVariable String gender) {
        //TODO: option.id, userDetails.gender.. will have to call 2 times for each post for now
        //Eg: postId-male, postId-female
        RestResponse response = voteService.getVotesAnalysisByPostIdAndGender(postId, gender);
        return ResponseEntity.ok(response);
    }

}

package com.tot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tot.pojo.RestResponse;
import com.tot.service.UserStatsService;

/**
 * @author karthik on 17/01/22.
 * @project totservices
 */

@RestController public class StatsController {

    @Autowired UserStatsService userStatsService;

    @GetMapping("/user/stats/{userId}")
    public ResponseEntity<RestResponse> getUserStats(@PathVariable String userId) {
        RestResponse response = userStatsService.getUserStats(userId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/user/stats/posts")
    public ResponseEntity<RestResponse> updatePostCount(@RequestParam String userId,
        @RequestParam(required = false) Long postsCount) {
        //if delete, call with postCount = -1;
        if (postsCount == null) {
            postsCount = 1l;
        }
        RestResponse response = userStatsService.updatePostCount(userId, postsCount);
        return ResponseEntity.ok(response);
    }

}

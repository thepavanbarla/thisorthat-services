package com.tot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tot.pojo.RestResponse;
import com.tot.service.BlockService;

/**
 * @author karthik on 02/07/22.
 * @project totservices
 */

@RestController
public class BlockController {

    @Autowired BlockService blockService;

    //block user
    @PostMapping("/block/{userId}") public ResponseEntity<RestResponse> blockUser(
        @PathVariable String userId) {
        RestResponse response = blockService.blockUser(userId);
        return ResponseEntity.ok(response);
    }

    //unblock user
    @PostMapping("/unblock/{userId}") public ResponseEntity<RestResponse> unblockUser(
        @PathVariable String userId) {
        RestResponse response = blockService.unblockUser(userId);
        return ResponseEntity.ok(response);
    }

    //get blocked users of a user
    @GetMapping("/blocked") public ResponseEntity<RestResponse> getBlockedUsers() {
        RestResponse response = blockService.getBlockedUsers();
        return ResponseEntity.ok(response);
    }

    //get users user is blocked by
    @GetMapping("/blockedBy") public ResponseEntity<RestResponse> getBlockedByUsers() {
        RestResponse response = blockService.getBlockedByUsers();
        return ResponseEntity.ok(response);
    }

    //feed and explore should not show posts from blocked

}

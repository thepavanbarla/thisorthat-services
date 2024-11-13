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

import com.tot.pojo.RestResponse;
import com.tot.pojo.User;
import com.tot.pojo.UserPreference;
import com.tot.service.MediaManagerService;
import com.tot.service.UserService;
import com.tot.utils.Constants;

/**
 * @author karthik on 17/08/21.
 * @project tot-services
 */

@RestController public class UserController {

    @Autowired UserService userService;
    @Autowired MediaManagerService mediaManagerService;

    @PutMapping("/user/create")
    public ResponseEntity<RestResponse> createUser(@RequestBody User user) {
        RestResponse response = userService.createUser(user);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/user/update")
    public ResponseEntity<RestResponse> updateUser(@RequestBody User user) {
        RestResponse response = userService.updateUser(user);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/user/update/privacy")
    public ResponseEntity<RestResponse> updateUserPrivacy(@RequestBody User user) {
        RestResponse response = userService.updateUserPrivacy(user.getIsPrivate());
        return ResponseEntity.ok(response);
    }

    @PutMapping("/user/profile/update")
    public ResponseEntity<RestResponse> updateUserProfile(@RequestBody User user) {
        RestResponse response = userService.updateUserProfile(user);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/user/cover")
    public ResponseEntity<RestResponse> updateUserCover(@RequestParam String cover) {
        RestResponse response = userService.updateUserCover(cover);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/user/username/{username}")
    public ResponseEntity<RestResponse> getUserByName(@PathVariable String username) {
        RestResponse response = userService.getUserByName(username, false);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<RestResponse> getUserById(@PathVariable String userId) {
        RestResponse response = userService.getUserById(userId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/follow/{tgtUserId}")
    public ResponseEntity<RestResponse> followUser(@PathVariable("tgtUserId") String tgtUserId) {
        RestResponse response = userService.followUser(tgtUserId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/unfollow/{tgtUserId}")
    public ResponseEntity<RestResponse> unfollowUser(@PathVariable("tgtUserId") String tgtUserId) {
        RestResponse response = userService.unfollowUser(tgtUserId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/follow_respond/{tgtUserId}/{status}")
    public ResponseEntity<RestResponse> respondToFollow(@PathVariable("tgtUserId") String tgtUserId,
        @PathVariable("status") String status) {
        //status should be accept or reject
        RestResponse response = userService.respondToFollow(tgtUserId, status);
        return ResponseEntity.ok(response);
    }

    @GetMapping({"/followers/{userId}"})
    public ResponseEntity<RestResponse> getFollowers(@PathVariable String userId,
        @RequestParam(required = false) Integer skip,
        @RequestParam(required = false) Integer limit) {
        if (skip == null || limit == null) {
            skip = Constants.DEFAULT_SKIP;
            limit = Constants.DEFAULT_LIMIT;
        }
        RestResponse response = userService.getFollowers(userId, skip, limit);
        return ResponseEntity.ok(response);
    }

    @GetMapping({"/following/{userId}"})
    public ResponseEntity<RestResponse> getFollowing(@PathVariable String userId,
		@RequestParam(required = false) Integer skip,
		@RequestParam(required = false) Integer limit) {
        if (skip == null || limit == null) {
            skip = Constants.DEFAULT_SKIP;
            limit = Constants.DEFAULT_LIMIT;
        }
        RestResponse response = userService.getFollowing(userId, skip, limit);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/follow/status/{srcUserId}/{tgtUserId}")
    public ResponseEntity<RestResponse> getFollowStatus(@PathVariable("srcUserId") String srcUserId,
        @PathVariable("tgtUserId") String tgtUserId) {
        RestResponse response = userService.getFollowStatus(srcUserId, tgtUserId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/user/upload_picture")
    public ResponseEntity<RestResponse> uploadFile(@RequestParam MultipartFile file) {
        RestResponse accountsResponse = mediaManagerService.uploadUserPicture(file);
        return ResponseEntity.ok(accountsResponse);
    }

    @GetMapping("/public/user/{username}")
    public ResponseEntity<RestResponse> getPublicUser(@PathVariable String username) {
        RestResponse response = userService.getUserByName(username, true);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/public/search/{username}")
    public ResponseEntity<RestResponse> searchUser(@PathVariable String username,
        @RequestParam(required = false) Integer skip,
        @RequestParam(required = false) Integer limit) {
        if (skip == null || limit == null) {
            skip = Constants.DEFAULT_SKIP;
            limit = Constants.DEFAULT_LIMIT;
        }
        RestResponse response = userService.searchUserByName(username, skip, limit);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/user_preference")
    public ResponseEntity<RestResponse> getUserPreferences() {
        RestResponse response = userService.getUserPreference();
        return ResponseEntity.ok(response);
    }

    @PutMapping("/user_preference/update")
    public ResponseEntity<RestResponse> updateUserProfile(@RequestBody UserPreference userPreference) {
        RestResponse response = userService.updateUserPreference(userPreference);
        return ResponseEntity.ok(response);
    }
    
    @PutMapping("/user/add_device/{deviceId}")
    public ResponseEntity<RestResponse> addUserDevice(@PathVariable("deviceId") String deviceId) {
        RestResponse response = userService.addUserDevice(deviceId);
        return ResponseEntity.ok(response);
    }
    
    @PutMapping("/user/remove_device/{deviceId}")
    public ResponseEntity<RestResponse> removeUserDevice(@PathVariable("deviceId") String deviceId) {
        RestResponse response = userService.removeUserDevice(deviceId);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/public/interests")
    public ResponseEntity<RestResponse> getAllInterests() {
        RestResponse response = userService.getAllInterests();
        return ResponseEntity.ok(response);
    }

}

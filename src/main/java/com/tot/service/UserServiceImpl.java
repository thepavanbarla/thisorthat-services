package com.tot.service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import com.tot.dao.BlockDao;
import com.tot.pojo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tot.dao.NotificationDao;
import com.tot.dao.UserDao;
import com.tot.utils.UserIdentityUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * @author karthik on 04/12/21.
 * @project totservices
 */
@Slf4j @Service public class UserServiceImpl implements UserService {

    @Autowired UserDao userDao;
    @Autowired NotificationDao notificationDao;
    @Autowired PushService pushService;
    @Autowired BlockDao blockDao;


    @Override public RestResponse createUser(User user) {
        userDao.createUser(user);
        return RestResponse.getSuccessResponse("User created successfully...");
    }

    @Override public RestResponse updateUser(User user) {
        userDao.updateUser(user);
        return RestResponse.getSuccessResponse("User updated successfully...");
    }

    @Override public RestResponse updateUserPrivacy(Boolean isPrivate) {
        String userId = UserIdentityUtils.getUserIdFromSecurityContext();
        userDao.updateUserPrivacy(isPrivate, userId);
        return RestResponse.getSuccessResponse("User updated successfully...");
    }

    @Override public RestResponse updateUserProfile(User user) {
        userDao.updateUserProfile(user);
        return RestResponse.getSuccessResponse("User profile updated successfully...");
    }

    @Override public RestResponse updateUserCover(String cover) {
        String userId = UserIdentityUtils.getUserIdFromSecurityContext();
        userDao.updateUserCover(cover, userId);
        return RestResponse.getSuccessResponse("User profile updated successfully...");
    }

    @Override public RestResponse getUserById(String userId) {
        String currentUserId = UserIdentityUtils.getUserIdFromSecurityContext();
        if(!currentUserId.equals(userId)){
            List<Block> blocked = blockDao.findByBlockerIdAndBlockedId(userId, currentUserId);
            if(blocked.size() > 0){
                return RestResponse.getFailedResponse(403, "This user has blocked you!");
            }
        }
        User user = userDao.getUserbyId(userId);
        return RestResponse.getSuccessResponse(user);
    }

    @Override public RestResponse getUserByName(String username, boolean isPublic) {
        User user = userDao.getUserbyUserName(username);
        if (isPublic && user.getIsPrivate()) {
            return RestResponse.getFailedResponse(403,
                "Access denied for fetching private profile using public api");
        }
        String currentUserId = UserIdentityUtils.getUserIdFromSecurityContext();
        if(!currentUserId.equals(user.getUserId())){
            List<Block> blocked = blockDao.findByBlockerIdAndBlockedId(user.getUserId(), currentUserId);
            if(blocked.size() > 0){
                return RestResponse.getFailedResponse(403, "This user has blocked you!");
            }
        }
        return RestResponse.getSuccessResponse(user);
    }

    @Override public RestResponse followUser(String tgtUser) {
        String userId = UserIdentityUtils.getUserIdFromSecurityContext();
        userDao.followUser(userId, tgtUser);
        User user = userDao.getUserbyId(tgtUser);
        User otherUser = userDao.getUserbyId(userId);
        if (!user.getIsPrivate()) {
            userDao.updateFollow(tgtUser, userId);
            CompletableFuture.runAsync(() -> {
                notificationDao.save(new Notification(tgtUser, userId, null, "follow", null,
                    Timestamp.from(Instant.now())));
                pushService.sendProfilePushNotificationToUser(tgtUser, "You have a new follower!", 
            		otherUser.getFullName() + " is now following you. Check their profile now. ", otherUser.getUserName());
            });
        } else {
            CompletableFuture.runAsync(() -> {
                notificationDao.save(new Notification(tgtUser, userId, null, "follow_request", null,
                    Timestamp.from(Instant.now())));
                pushService.sendProfilePushNotificationToUser(tgtUser, "You have a new follow request!", 
                		otherUser.getFullName() + " has requested to follow you. Check their profile now. ", otherUser.getUserName());
            });
        }
        return RestResponse.getSuccessResponse("success");
    }

    @Override public RestResponse unfollowUser(String tgtUser) {
        String userId = UserIdentityUtils.getUserIdFromSecurityContext();
        userDao.unfollowUser(userId, tgtUser);
        return RestResponse.getSuccessResponse("success");
    }

    @Override public RestResponse respondToFollow(String tgtUser, String status) {
        String userId = UserIdentityUtils.getUserIdFromSecurityContext();
        User otherUser = userDao.getUserbyId(userId);
        switch (status) {
            case "accept":
                userDao.updateFollow(userId, tgtUser);
                CompletableFuture.runAsync(() -> {
                    notificationDao.save(
                        new Notification(tgtUser, userId, null, "follow_accept", null,
                            Timestamp.from(Instant.now())));
                    notificationDao
                        .deleteByUserIdAndSenderIdAndType(userId, tgtUser, "follow_request");
                    pushService.sendProfilePushNotificationToUser(tgtUser, "Follow request accepted!", 
                    		otherUser.getFullName() + " has accepted your follow request. Check their profile now. ", otherUser.getUserName());
                });
                break;
            case "reject":
                userDao.deleteFollow(userId, tgtUser);
                CompletableFuture.runAsync(() -> {
                    notificationDao
                        .deleteByUserIdAndSenderIdAndType(userId, tgtUser, "follow_request");
                });
                break;
            default:
                log.error("Cannot respond with status: " + status);
                RestResponse.getFailedResponse(420, "Cannot respond with status: " + status);
        }
        return RestResponse.getSuccessResponse("success");
    }

    @Override public RestResponse getFollowers(String userId, int skip, int limit) {
        List<User> users = userDao.getFollowers(userId, skip, limit);
        return RestResponse.getSuccessResponse(users);
    }

    @Override public RestResponse getFollowing(String userId, int skip, int limit) {
        List<User> users = userDao.getFollowing(userId, skip, limit);
        return RestResponse.getSuccessResponse(users);
    }

    @Override public RestResponse getFollowStatus(String srcUserId, String tgtUserId) {
        Follow follow = userDao.getFollowStatus(srcUserId, tgtUserId);
        return RestResponse.getSuccessResponse(follow);
    }

    @Override public RestResponse getUserPreference() {
    	String userId = UserIdentityUtils.getUserIdFromSecurityContext();
        UserPreference userPreference = userDao.getUserPreference(userId);
        return  RestResponse.getSuccessResponse(userPreference);
    }

    @Override public RestResponse updateUserPreference(UserPreference userPreference) {
    	String userId = UserIdentityUtils.getUserIdFromSecurityContext();
    	userPreference.setUserId(userId);
        userDao.updateUserPreference(userPreference);
        return RestResponse.getSuccessResponse("success");
    }

	@Override
	public RestResponse addUserDevice(String deviceId) {
		String userId = UserIdentityUtils.getUserIdFromSecurityContext();
		userDao.addUserDevice(userId, deviceId);
		return RestResponse.getSuccessResponse("success");
	}

	@Override
	public RestResponse removeUserDevice(String deviceId) {
		String userId = UserIdentityUtils.getUserIdFromSecurityContext();
		userDao.removeUserDevice(userId, deviceId);
		return RestResponse.getSuccessResponse("success");
	}

    @Override public RestResponse searchUserByName(String username, int skip, int limit) {
        List<BasicUserDetails> users = userDao.searchUserByName(username, skip, limit);
        return RestResponse.getSuccessResponse(users);
    }

	@Override
	public RestResponse getAllInterests() {
		List<UserInterest> interests = userDao.getAllInterests();
		return RestResponse.getSuccessResponse(interests);
	}
    
    
}

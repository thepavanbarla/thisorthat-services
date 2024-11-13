package com.tot.service;

import com.tot.pojo.RestResponse;
import com.tot.pojo.User;
import com.tot.pojo.UserPreference;

/**
 * @author karthik on 17/08/21.
 * @project tot-services
 */

public interface UserService {

    RestResponse createUser(User user);

    RestResponse updateUser(User user);
    
    RestResponse updateUserPrivacy(Boolean isPrivate);
    
    RestResponse updateUserProfile(User user);
    
    RestResponse updateUserCover(String cover);

    RestResponse getUserById(String userId);

    RestResponse getUserByName(String username, boolean isPublic);

    RestResponse followUser(String tgtUser);

    RestResponse unfollowUser(String tgtUser);

    RestResponse respondToFollow(String tgtUser, String status);

    RestResponse getFollowers(String userId, int skip, int limit);

    RestResponse getFollowing(String userId, int skip, int limit);

    RestResponse getFollowStatus(String srcUserId, String tgtUserId);

    RestResponse getUserPreference();

    RestResponse updateUserPreference(UserPreference userPreference);
    
    RestResponse addUserDevice(String deviceId);
    
    RestResponse removeUserDevice(String deviceId);

    RestResponse searchUserByName(String username, int skip, int limit);
    
    RestResponse getAllInterests();
}

package com.tot.dao;

import java.util.List;

import com.tot.pojo.BasicUserDetails;
import com.tot.pojo.Follow;
import com.tot.pojo.Tag;
import com.tot.pojo.User;
import com.tot.pojo.UserInterest;
import com.tot.pojo.UserPreference;

public interface UserDao {

    String createUser(User user);

    String updateUser(User user);

    String updateUserProfile(User user);

    String updateUserPrivacy(Boolean isPrivate, String userId);

    String updateUserCover(String cover, String userId);

    User getUserbyUserName(String username);

    User getUserbyId(String userId);

    BasicUserDetails getBasicUserbyId(String userId);

    String followUser(String userId, String tgtUser);

    String unfollowUser(String userId, String tgtUser);

    String updateFollow(String userId, String tgtUser);

    String deleteFollow(String userId, String tgtUser);

    List<User> getFollowers(String userId, int skip, int limit);

    List<User> getFollowing(String userId, int skip, int limit);

    List<String> getFollowingIds(String userId);

    Follow getFollowStatus(String srcUserId, String tgtUserId);

    UserPreference getUserPreference(String userId);

    void updateUserPreference(UserPreference userPreference);
    
    void addUserDevice(String userId, String deviceId);
    
    void removeUserDevice(String userId, String deviceId);
    
    List<String> getUserDevices(String userId);

    List<BasicUserDetails> searchUserByName(String username, int skip, int limit);
    
    List<Tag> findTagsForAutocomplete(String tag);
    
    void addTag(String tag);
    
    List<UserInterest> getAllInterests();
    
    List<UserInterest> getUserInterests(List<String> interests);
}

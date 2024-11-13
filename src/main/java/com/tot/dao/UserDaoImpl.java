package com.tot.dao;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.tot.pojo.BasicUserDetails;
import com.tot.pojo.Follow;
import com.tot.pojo.Tag;
import com.tot.pojo.User;
import com.tot.pojo.UserInterest;
import com.tot.pojo.UserPreference;

/**
 * @author karthik on 22/08/21.
 * @project tot-services
 */

@Repository public class UserDaoImpl implements UserDao {

    @Value("${sql.user.create}") String createUserSqlScript;
    @Value("${sql.user.update}") String updateUserSqlScript;
    @Value("${sql.user.update.cover}") String updateUserCoverSqlScript;
    @Value("${sql.user.update.profile}") String updateUserProfileSqlScript;
    @Value("${sql.user.update.privacy}") String updateUserPrivacySqlScript;
    @Value("${sql.user.by.id}") String getUserById;
    @Value("${sql.user.by.username}") String getUserByUsername;
    @Value("${sql.insert.to.follow}") String insertToFollow;
    @Value("${sql.update.follow}") String updateFollow;
    @Value("${sql.delete.from.follow}") String deleteFromFollow;
    @Value("${sql.user.fetch.followers}") String fetchFollowersSqlScript;
    @Value("${sql.user.fetch.following}") String fetchFollowingSqlScript;
    @Value("${sql.user.following.ids}") String fetchFollowingIdsSqlScript;
    @Value("${sql.user.follow.status}") String fetchFollowStatusSqlScript;
    @Value("${sql.user.preference.get}") String getUserPreferenceByUserId;
    @Value("${sql.user.preference.update}") String updateUserPreferenceSqlScript;
    @Value("${sql.user.add.device}") String addUserDeviceSqlScript;
    @Value("${sql.user.remove.device}") String removeUserDeviceSqlScript;
    @Value("${sql.user.fetch.devices}") String fetchUserDevicesSqlScript;
    @Value("${sql.user.search.username}") String searchByName;
    @Value("${sql.tags.search}") String searchTag;
    @Value("${sql.tags.add}") String addTag;
    @Value("${sql.interests.fetch.all}") String getAllInterests;
    @Value("${sql.interests.fetch.user}") String getUserInterests;

    @Autowired JdbcTemplate jdbcTemplate;

    @Override public String createUser(User user) {
        jdbcTemplate.update(con -> {
            PreparedStatement statement =
                con.prepareStatement(createUserSqlScript, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, user.getUserId());
            statement.setString(2, user.getUserName());
            statement.setString(3, user.getEmail());
            statement.setString(4, user.getPassword());
            statement.setString(5, user.getPhoneNumber());
            statement.setString(6, user.getGender());
            statement.setDate(7, user.getDob());
            statement.setString(8, user.getFullName());
            statement.setString(9, user.getBio());
            statement.setString(10, user.getProfilePicture());
            return statement;
        });
        return "success";
    }

    @Override public String updateUser(User user) {
        jdbcTemplate.update(con -> {
            PreparedStatement statement =
                con.prepareStatement(updateUserSqlScript, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, user.getUserName());
            statement.setString(2, user.getGender());
            statement.setDate(3, user.getDob());
            statement.setString(4, user.getFullName());
            statement.setString(5, user.getProfilePicture());
            statement.setString(6, user.getUserId());
            return statement;
        });
        return "success";
    }

    @Override public String updateUserProfile(User user) {
        jdbcTemplate.update(con -> {
            PreparedStatement statement =
                con.prepareStatement(updateUserProfileSqlScript, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, user.getUserName());
            statement.setString(2, user.getFullName());
            statement.setString(3, user.getProfilePicture());
            statement.setString(4, user.getWebsite());
            statement.setString(5, user.getBio());
            statement.setString(6, user.getUserId());
            return statement;
        });
        return "success";
    }

    @Override public String updateUserPrivacy(Boolean isPrivate, String userId) {
        jdbcTemplate.update(con -> {
            PreparedStatement statement =
                con.prepareStatement(updateUserPrivacySqlScript, Statement.RETURN_GENERATED_KEYS);
            statement.setBoolean(1, isPrivate);
            statement.setString(2, userId);
            return statement;
        });
        return "success";
    }

    @Override public String updateUserCover(String cover, String userId) {
        jdbcTemplate.update(con -> {
            PreparedStatement statement =
                con.prepareStatement(updateUserCoverSqlScript, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, cover);
            statement.setString(2, userId);
            return statement;
        });
        return "success";
    }

    @Override public User getUserbyUserName(String username) {
        try {
            return jdbcTemplate.queryForObject(getUserByUsername, new Object[] {username},
                (rs, rowNum) -> new User(rs.getString("user_id"), rs.getString("user_name"),
                    rs.getString("email"), rs.getString("password"), rs.getString("phone_number"),
                    rs.getString("gender"), rs.getDate("dob"), rs.getString("full_name"),
                    rs.getString("bio"), rs.getString("profile_picture"),
                    rs.getString("cover_picture"), rs.getBoolean("is_private"),
                    rs.getTimestamp("created_at"), rs.getTimestamp("last_modified"),
                    rs.getBoolean("is_active"), rs.getString("website"), rs.getBoolean("is_channel")));
        } catch (EmptyResultDataAccessException ex) {
            return null;
        }
    }

    @Override public User getUserbyId(String userId) {
        try {
            return jdbcTemplate.queryForObject(getUserById, new Object[] {userId},
                (rs, rowNum) -> new User(rs.getString("user_id"), rs.getString("user_name"),
                    rs.getString("email"), rs.getString("password"), rs.getString("phone_number"),
                    rs.getString("gender"), rs.getDate("dob"), rs.getString("full_name"),
                    rs.getString("bio"), rs.getString("profile_picture"),
                    rs.getString("cover_picture"), rs.getBoolean("is_private"),
                    rs.getTimestamp("created_at"), rs.getTimestamp("last_modified"),
                    rs.getBoolean("is_active"), rs.getString("website"), rs.getBoolean("is_channel")));
        } catch (EmptyResultDataAccessException ex) {
            return null;
        }
    }

    @Override public BasicUserDetails getBasicUserbyId(String userId) {
        try {
            return jdbcTemplate.queryForObject(getUserById, new Object[] {userId},
                (rs, rowNum) -> new BasicUserDetails(rs.getString("user_id"),
                    rs.getString("user_name"), rs.getDate("dob"), rs.getString("full_name"),
                    rs.getString("gender"), rs.getString("profile_picture"),
                    rs.getString("phone_number")));
        } catch (EmptyResultDataAccessException ex) {
            return null;
        }
    }

    @Override public String followUser(String userId, String tgtUser) {
        jdbcTemplate.update(con -> {
            PreparedStatement statement =
                con.prepareStatement(insertToFollow, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, userId);
            statement.setString(2, tgtUser);
            return statement;
        });
        return "success";
    }

    @Override public String unfollowUser(String userId, String tgtUser) {
        jdbcTemplate.update(con -> {
            PreparedStatement statement =
                con.prepareStatement(deleteFromFollow, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, userId);
            statement.setString(2, tgtUser);
            return statement;
        });
        return "success";
    }

    @Override public String updateFollow(String userId, String tgtUser) {
        jdbcTemplate.update(con -> {
            PreparedStatement statement =
                con.prepareStatement(updateFollow, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, userId);
            statement.setString(2, tgtUser);
            return statement;
        });
        return "success";
    }

    @Override public String deleteFollow(String userId, String tgtUser) {
        jdbcTemplate.update(con -> {
            PreparedStatement statement =
                con.prepareStatement(deleteFromFollow, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, userId);
            statement.setString(2, tgtUser);
            return statement;
        });
        return "success";
    }

    @Override public List<User> getFollowers(String userId, int skip, int limit) {
        return jdbcTemplate.query(fetchFollowersSqlScript, new Object[] {userId, limit, skip},
            (rs, rowNum) -> new User(rs.getString("user_name"), rs.getString("full_name"),
                rs.getString("profile_picture"), rs.getString("user_id")));
    }

    @Override public List<User> getFollowing(String userId, int skip, int limit) {
        return jdbcTemplate.query(fetchFollowingSqlScript, new Object[] {userId, limit, skip},
            (rs, rowNum) -> new User(rs.getString("user_name"), rs.getString("full_name"),
                rs.getString("profile_picture"), rs.getString("user_id")));
    }

    @Override public List<String> getFollowingIds(String userId) {
        return jdbcTemplate.query(fetchFollowingIdsSqlScript, new Object[] {userId},
            (rs, rowNum) -> rs.getString("tgt_user_id"));
    }

    @Override public Follow getFollowStatus(String srcUserId, String tgtUserId) {
        try {
            return jdbcTemplate
                .queryForObject(fetchFollowStatusSqlScript, new Object[] {srcUserId, tgtUserId},
                    (rs, rowNum) -> new Follow(rs.getString("src_user_id"),
                        rs.getString("tgt_user_id"), rs.getBoolean("accepted"),
                        rs.getTimestamp("accepted_at"), rs.getTimestamp("created_at")));
        } catch (EmptyResultDataAccessException ex) {
            return null;
        }
    }

    @Override public UserPreference getUserPreference(String userId) {
        try {
            return jdbcTemplate.queryForObject(getUserPreferenceByUserId, new Object[] {userId},
                (rs, rowNum) -> new UserPreference(rs.getString("user_id"),
                    rs.getBoolean("allow_notifications"), rs.getBoolean("allow_post_notifications"),
                    rs.getBoolean("allow_follow_notifications")));
        } catch (EmptyResultDataAccessException ex) {
            return null;
        }
    }

    @Override public void updateUserPreference(UserPreference userPreference) {
        jdbcTemplate.update(con -> {
            PreparedStatement statement = con.prepareStatement(updateUserPreferenceSqlScript,
                Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, userPreference.getUserId());
            statement.setBoolean(2, userPreference.getAllowNotifications());
            statement.setBoolean(3, userPreference.getAllowPostNotifications());
            statement.setBoolean(4, userPreference.getAllowFollowNotifications());
            return statement;
        });
    }

	@Override
	public void addUserDevice(String userId, String deviceId) {
		jdbcTemplate.update(con -> {
            PreparedStatement statement = con.prepareStatement(addUserDeviceSqlScript,
                Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, userId);
            statement.setString(2, deviceId);
            return statement;
        });
		
	}

	@Override
	public void removeUserDevice(String userId, String deviceId) {
		jdbcTemplate.update(con -> {
            PreparedStatement statement = con.prepareStatement(removeUserDeviceSqlScript,
                Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, userId);
            statement.setString(2, deviceId);
            return statement;
        });
	}

	@Override
	public List<String> getUserDevices(String userId) {
		return jdbcTemplate.query(fetchUserDevicesSqlScript, new Object[] {userId},
            (rs, rowNum) -> rs.getString("device_fcm_token"));
	}

    @Override public List<BasicUserDetails> searchUserByName(String username, int skip, int limit) {
        try {
        	String likePhrase = new StringBuilder("%").append(username.toLowerCase()).append("%").toString();
            return jdbcTemplate.query(searchByName, new Object[] {likePhrase, likePhrase, limit, skip},
                (rs, rowNum) -> new BasicUserDetails(rs.getString("user_id"),
                    rs.getString("user_name"), rs.getDate("dob"), rs.getString("full_name"),
                    rs.getString("gender"), rs.getString("profile_picture"),
                    rs.getString("phone_number")));
        } catch (EmptyResultDataAccessException ex) {
            return Collections.emptyList();
        }
    }

	@Override
	public List<Tag> findTagsForAutocomplete(String tag) {
		try {
            return jdbcTemplate.query(searchTag, new Object[] {new StringBuilder(tag).append("%").toString()},
                (rs, rowNum) -> new Tag(rs.getString("tag"), rs.getInt("post_count")));
        } catch (EmptyResultDataAccessException ex) {
            return Collections.emptyList();
        }
	}
	
	@Override
	public void addTag(String tag) {
		jdbcTemplate.update(con -> {
            PreparedStatement statement =
                con.prepareStatement(addTag, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, tag);
            return statement;
        });
	}

	@Override
	public List<UserInterest> getAllInterests() {
		try {
			return jdbcTemplate.query(getAllInterests, new Object[] {},
	            (rs, rowNum) -> new UserInterest(rs.getString("interest_tag"), rs.getString("interest_name"), rs.getString("picture")));
		} catch (EmptyResultDataAccessException ex) {
            return Collections.emptyList();
        }
	}
	
	@Override
	public List<UserInterest> getUserInterests(List<String> interests) {
		try {
			return jdbcTemplate.query(getUserInterests.replace("?", interests.stream()
                    .map(v -> "?")
                    .collect(Collectors.joining(", "))), interests.toArray(),
	            (rs, rowNum) -> new UserInterest(rs.getString("interest_tag"), rs.getString("interest_name"), rs.getString("picture")));
		} catch (EmptyResultDataAccessException ex) {
            return Collections.emptyList();
        }
	}

}

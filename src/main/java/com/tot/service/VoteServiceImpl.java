package com.tot.service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.tot.dao.NotificationDao;
import com.tot.dao.PostDao;
import com.tot.dao.UserDao;
import com.tot.dao.VoteDao;
import com.tot.pojo.Notification;
import com.tot.pojo.PostStats;
import com.tot.pojo.RestResponse;
import com.tot.pojo.User;
import com.tot.pojo.UserVote;
import com.tot.utils.UserIdentityUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * @author karthik on 20/01/22.
 * @project totservices
 */
@Slf4j
@Service public class VoteServiceImpl implements VoteService {

    @Autowired VoteDao voteDao;
    @Autowired MongoTemplate mongoTemplate;
    @Autowired UserDao userDao;
    @Autowired NotificationDao notificationDao;
    @Autowired PostDao postDao;
    @Autowired PushService pushService;

    @Override public RestResponse saveVote(UserVote vote) {
        RestResponse restResponse = null;
            List<UserVote> oldVote = voteDao.findByUserIdAndPostId(vote.getUserId(), 
            		vote.getPostId(), PageRequest.of(0, 1));
            if(oldVote.size() > 0){
            	vote.setVoteId(oldVote.get(0).getVoteId());                
            	restResponse = handleExistingVote(vote, oldVote.get(0));
            } else {
                restResponse = handleNewVote(vote);
            }
        return restResponse;
    }

    private RestResponse handleNewVote(UserVote vote) {
        String userId = UserIdentityUtils.getUserIdFromSecurityContext();
        UserVote result = voteDao.save(vote);
        updatePostStats(vote);
        try{
            sendVoteNotification(vote, userId);
        } catch(Exception e){
            //non blocking attempt - NOP
        }
        return RestResponse.getSuccessResponse(result.getVoteId());
    }

    private RestResponse handleExistingVote(UserVote vote, UserVote oldVote) {
        if(oldVote.getOption().getId().equalsIgnoreCase(vote.getOption().getId())){
            //No need to change anything
            return RestResponse.getSuccessResponse(vote.getVoteId());
        }
        String userId = UserIdentityUtils.getUserIdFromSecurityContext();
        UserVote result = voteDao.save(vote);
//        sendVoteNotification(vote, userId);

        updateExistingPostStats(vote, oldVote);
        return RestResponse.getSuccessResponse(result.getVoteId());
    }

    private void sendVoteNotification(UserVote vote, String userId) {
        String postUserId = postDao.findById(vote.getPostId()).get().getUserId();
        User voteUser = userDao.getUserbyId(userId);

        if (!UserIdentityUtils.getUserIdFromSecurityContext().equals(postUserId)) {
            notificationDao.save(
                new Notification(postUserId, vote.getUserId(), vote.getPostId(), "vote", null,
                    Timestamp.from(Instant.now())));
            pushService.sendPostPushNotificationToUser(postUserId, "You have a new vote!",
                voteUser.getFullName() + " has voted on your post. Check it out now. ",
                vote.getPostId());
        }
    }

    private void updatePostStats(UserVote vote) {
        String postId = vote.getPostId();
        String optionId = vote.getOption().getId();
        String gender = vote.getUserDetails().getGender().toLowerCase();

        Query query = new Query();

        query.addCriteria(
            Criteria.where("options.optionId").is(optionId));//where("postId").is(postId).and
        Update update = new Update();
        update.set("postId", postId);
        update.inc("totalVotes");
        if (gender.equalsIgnoreCase("male")) {
            update.inc("totalVotesMale");
            update.inc("options.$.totalOptionCount");
            update.inc("options.$.optionCountMale");
        } else {
            update.inc("totalVotesFemale");
            update.inc("options.$.totalOptionCount");
            update.inc("options.$.optionCountFemale");
        }

        mongoTemplate.upsert(query, update, PostStats.class);

    }

    private void updateExistingPostStats(UserVote vote, UserVote oldVote) {
        String postId = vote.getPostId();
        String newOptionId = vote.getOption().getId();
        String oldOptionId = oldVote.getOption().getId();
        String gender = vote.getUserDetails().getGender().toLowerCase();

        Query query1 = new Query();

        query1.addCriteria(
            Criteria.where("options.optionId").is(newOptionId));//where("postId").is(postId).and
        Update update1 = new Update();
        update1.set("postId", postId);
        if (gender.equalsIgnoreCase("male")) {
            update1.inc("options.$.totalOptionCount");
            update1.inc("options.$.optionCountMale");
        } else {
            update1.inc("options.$.totalOptionCount");
            update1.inc("options.$.optionCountFemale");
        }

        Query query2 = new Query();

        query2.addCriteria(
            Criteria.where("options.optionId").is(oldOptionId));//where("postId").is(postId).and
        Update update2 = new Update();
        update2.set("postId", postId);
        if (gender.equalsIgnoreCase("male")) {
            update2.inc("options.$.totalOptionCount", -1);
            update2.inc("options.$.optionCountMale", -1);
        } else {
            update2.inc("options.$.totalOptionCount", -1);
            update2.inc("options.$.optionCountFemale", -1);
        }

            mongoTemplate.upsert(query1, update1, PostStats.class);
            mongoTemplate.upsert(query2, update2, PostStats.class);

    }

    @Override public RestResponse getVotesByPostId(String postId, int skip, int limit) {
        List<UserVote> result = voteDao.findByPostId(postId, PageRequest.of(skip, limit));
        //TODO: remove from here and add from UI (add basicuserdetails in vote)
        result.forEach(v -> v.setUserDetails(userDao.getBasicUserbyId(v.getUserId())));
        return RestResponse.getSuccessResponse(result);
    }

    @Override public RestResponse getVotesByUserId(String userId, int skip, int limit) {
        List<UserVote> result = voteDao.findByUserId(userId, PageRequest.of(skip, limit));
        return RestResponse.getSuccessResponse(result);
    }

    @Override public RestResponse getVotesByUserIdAndPostId(String userId, String postId) {
        UserVote result = getUserVoteOnPost(userId, postId);
        return RestResponse.getSuccessResponse(result);
    }

    private UserVote getUserVoteOnPost(String userId, String postId) {
		List<UserVote> sessionUserVotes = voteDao.findByUserIdAndPostId(userId, postId, PageRequest.of(0, 1));
		if(sessionUserVotes.size() > 0) {
			return sessionUserVotes.get(0);
		}
		return null;
	}
    
    @Override public RestResponse getVotesById(String optionId) {
        List<UserVote> result = voteDao.findByOptionId(optionId);
        //TODO: remove from here and add from UI (add basicuserdetails in vote)
        result.forEach(v -> v.setUserDetails(userDao.getBasicUserbyId(v.getUserId())));
        return RestResponse.getSuccessResponse(result);
    }

    @Override
    public RestResponse getVotesAnalysisByOptionIdAndGender(String optionId, String gender) {
        List<UserVote> result =
            voteDao.findByOptionIdAndUserDetailsGenderIgnoreCase(optionId, gender);
        return RestResponse.getSuccessResponse(result);
    }

    @Override public RestResponse getVotesAnalysisByPostIdAndGender(String postId, String gender) {
        List<UserVote> result = voteDao.findByPostIdAndUserDetailsGenderIgnoreCase(postId, gender);
        return RestResponse.getSuccessResponse(result);
    }
}

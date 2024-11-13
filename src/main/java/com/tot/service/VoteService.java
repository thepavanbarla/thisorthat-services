package com.tot.service;

import com.tot.pojo.RestResponse;
import com.tot.pojo.UserVote;

public interface VoteService {

    RestResponse saveVote(UserVote vote);

    RestResponse getVotesByPostId(String postId, int skip, int limit);

    RestResponse getVotesByUserId(String userId, int skip, int limit);

    RestResponse getVotesByUserIdAndPostId(String userId, String postId);

    RestResponse getVotesById(String optionId);

    RestResponse getVotesAnalysisByOptionIdAndGender(String optionId, String gender);

    RestResponse getVotesAnalysisByPostIdAndGender(String postId, String gender);
}

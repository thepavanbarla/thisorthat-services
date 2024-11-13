package com.tot.service;

import com.tot.dao.PostStatsDao;
import com.tot.pojo.PostStats;
import com.tot.pojo.RestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author karthik on 21/01/22.
 * @project totservices
 */

@Service public class PostStatsServiceImpl implements PostStatsService {

    @Autowired PostStatsDao postStatsDao;

    @Override public RestResponse getPostStatsByPostId(String postId) {
        Optional<PostStats> postStats = postStatsDao.findById(postId);
        if (postStats.isPresent()) {
            return RestResponse.getSuccessResponse(postStats.get());
        } else {
            return RestResponse.getFailedResponse(420, "Post Stats does not exists...");
        }
    }
}

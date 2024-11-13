package com.tot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tot.dao.UserStatsDao;
import com.tot.pojo.RestResponse;

/**
 * @author karthik on 17/01/22.
 * @project totservices
 */

@Service public class UserStatsServiceImpl implements UserStatsService {

    @Autowired UserStatsDao userStatsDao;

    @Override public RestResponse getUserStats(String userId) {
        return RestResponse.getSuccessResponse(userStatsDao.getUserStats(userId));
    }

    @Override public RestResponse updatePostCount(String userId, Long postsCount) {
        return RestResponse.getSuccessResponse(userStatsDao.updatePostCount(userId, postsCount));
    }
}

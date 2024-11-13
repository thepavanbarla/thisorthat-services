package com.tot.service;

import com.tot.pojo.RestResponse;

/**
 * @author karthik on 17/01/22.
 * @project totservices
 */

public interface UserStatsService {

    RestResponse getUserStats(String userId);

    RestResponse updatePostCount(String userId, Long postsCount);
}

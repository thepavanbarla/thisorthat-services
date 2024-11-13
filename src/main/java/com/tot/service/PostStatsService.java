package com.tot.service;

import com.tot.pojo.RestResponse;

public interface PostStatsService {
    RestResponse getPostStatsByPostId(String postId);
}

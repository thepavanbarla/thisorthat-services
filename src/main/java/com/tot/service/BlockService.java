package com.tot.service;

import com.tot.pojo.RestResponse;

public interface BlockService {

    RestResponse blockUser(String blockedId);
    RestResponse unblockUser(String blockedId);

    RestResponse getBlockedUsers();
    RestResponse getBlockedByUsers();


}

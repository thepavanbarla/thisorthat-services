package com.tot.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tot.dao.BlockDao;
import com.tot.dao.UserDao;
import com.tot.pojo.Block;
import com.tot.pojo.RestResponse;
import com.tot.utils.UserIdentityUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * @author karthik on 02/07/22.
 * @project totservices
 */

@Service
@Slf4j public class BlockServiceImpl implements BlockService {

    @Autowired BlockDao blockDao;
    @Autowired UserDao userDao;

    @Override public RestResponse blockUser(String blockedId) {
        String blockerId = UserIdentityUtils.getUserIdFromSecurityContext();
        Block block = new Block();
        block.setBlockerId(blockerId);
        block.setBlockedId(blockedId);
        blockDao.save(block);
        //remove following on both sides
        try {
            userDao.unfollowUser(blockerId, blockedId);
            userDao.unfollowUser(blockedId, blockerId);
        } catch (Exception e) {
            log.error("Failed to unfollow on block", e);
        }
        return RestResponse.getSuccessResponse("success");
    }

    @Override public RestResponse unblockUser(String blockedId) {
        String blockerId = UserIdentityUtils.getUserIdFromSecurityContext();
        Block block = new Block();
        block.setBlockerId(blockerId);
        block.setBlockedId(blockedId);
        blockDao.delete(block);
        return RestResponse.getSuccessResponse("success");
    }

    @Override public RestResponse getBlockedUsers() {
        String userId = UserIdentityUtils.getUserIdFromSecurityContext();
        List<Block> blocked = blockDao.findByBlockerId(userId);
        return RestResponse.getSuccessResponse(blocked);
    }

    @Override public RestResponse getBlockedByUsers() {
        String userId = UserIdentityUtils.getUserIdFromSecurityContext();
        List<Block> blockedBy = blockDao.findByBlockedId(userId);
        return RestResponse.getSuccessResponse(blockedBy);
    }
}

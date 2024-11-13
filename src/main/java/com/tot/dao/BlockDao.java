package com.tot.dao;

import com.tot.pojo.Block;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author karthik on 02/07/22.
 * @project totservices
 */


@Repository public interface BlockDao extends MongoRepository<Block, String> {


    List<Block> findByBlockerId(String userId);

    List<Block> findByBlockedId(String userId);

    List<Block> findByBlockerIdAndBlockedId(String blockerId, String blockedId);
}

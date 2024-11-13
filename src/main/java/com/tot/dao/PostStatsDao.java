package com.tot.dao;

    import com.tot.pojo.PostStats;
    import org.springframework.data.mongodb.repository.MongoRepository;
    import org.springframework.stereotype.Repository;

/**
 * @author karthik on 20/01/22.
 * @project totservices
 */

@Repository public interface PostStatsDao extends MongoRepository<PostStats, String> {


}

package com.tot.dao;

import java.sql.PreparedStatement;
import java.sql.Statement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.tot.pojo.UserStats;

/**
 * @author karthik on 17/01/22.
 * @project totservices
 */

@Repository public class UserStatsDao {

    @Autowired JdbcTemplate jdbcTemplate;

    @Value("${sql.user.stats}") String getUserStatsSqlScript;
    @Value("${sql.user.stats.update.posts}") String updatePostsCountSqlScript;

    public UserStats getUserStats(String userId) {
        try {
            return jdbcTemplate.queryForObject(getUserStatsSqlScript, new Object[] {userId},
                (rs, rowNum) -> new UserStats(rs.getString("user_id"), rs.getLong("posts_count"),
                    rs.getLong("followers_count"), rs.getLong("following_count")));
        } catch (EmptyResultDataAccessException ex) {
            return null;
        }
    }

    public String updatePostCount(String userId, Long postsCount) {
        jdbcTemplate.update(con -> {
            PreparedStatement statement =
                con.prepareStatement(updatePostsCountSqlScript, Statement.RETURN_GENERATED_KEYS);
            statement.setLong(2, postsCount);
            statement.setString(1, userId);
            return statement;
        });

        return "success";
    }
}

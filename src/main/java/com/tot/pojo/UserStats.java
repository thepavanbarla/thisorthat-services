package com.tot.pojo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.relational.core.mapping.Table;

/**
 * @author karthik on 17/01/22.
 * @project totservices
 */
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @JsonInclude(JsonInclude.Include.NON_NULL)
@Table("user_stats") public class UserStats {

    private String userId;
    private Long postsCount;
    private Long followerCount;
    private Long followingCount;

}

package com.tot.pojo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.sql.Timestamp;

/**
 * @author karthik on 06/12/21.
 * @project totservices
 */

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @JsonInclude(JsonInclude.Include.NON_NULL)
@Document("tot_comments") public class Comment {

    @Id private String commentId;
    private String userId;
    private String postId;
    private String comment;
    private Timestamp createdTime;
    private User userDetails;

}

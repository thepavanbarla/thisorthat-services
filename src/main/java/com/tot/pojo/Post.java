package com.tot.pojo;

import java.sql.Timestamp;
import java.util.List;
import java.util.Set;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author karthik on 12/08/21.
 * @project tot-services
 */
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @JsonInclude(JsonInclude.Include.NON_NULL)
@Document("tot_post") public class Post {

    @Id protected String postId;
    @TextIndexed protected String userId;
    @TextIndexed protected String context;
    protected List<TOTOption> options;//store pics or text of options
    //store tags or categories applicable to post
    @TextIndexed protected Set<String> categories;
    protected Timestamp createdTime;
    protected Boolean isPrivate = false;
    protected Boolean isShare = false;
    protected long numVotes = 0;
    protected long numComments = 0;
    protected Boolean isDeleted = false;
    protected User userDetails;
    protected String gyan;
    protected List<String> tags;
    protected String type; //draft or post (default = post)
    protected String draftPostId;
    protected User draftPostUserDetails;
    protected String draftPostUserId;
    

}

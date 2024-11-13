package com.tot.pojo;

/**
 * @author karthik on 19/01/22.
 * @project totservices
 */

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @JsonInclude(JsonInclude.Include.NON_NULL)
@Document("tot_vote")
@CompoundIndexes({@CompoundIndex(name = "user_post_vote", def = "{'userId' : 1, 'postId': 1}")})
public class UserVote {

    @Id String voteId;
    String userId;
    BasicUserDetails userDetails;//fill while saving to mongo or from UI
    String postId;
    TOTOption option;

}


//get all votes for a user
//get all posts a user voted on
//get userId, username, full_name, age, gender where postId = p and vote.id = v;

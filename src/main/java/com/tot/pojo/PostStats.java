package com.tot.pojo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

/**
 * @author karthik on 20/01/22.
 * @project totservices
 */

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @JsonInclude(JsonInclude.Include.NON_NULL)
@Document("post_stats") public class PostStats {

    @Id String postId;
    Long totalVotes;
    Long totalVotesMale;
    Long totalVotesFemale;
    List<OptionStats> options = new ArrayList<>();

}

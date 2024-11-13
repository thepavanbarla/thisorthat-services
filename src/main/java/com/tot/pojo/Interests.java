package com.tot.pojo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/**
 * @author karthik on 15/01/22.
 * @project totservices
 */

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @JsonInclude(JsonInclude.Include.NON_NULL)
@Document("tot_interests") public class Interests {
    @Id private String interestId;
    @TextIndexed private String userId;
    @TextIndexed private List<String> interests;

}

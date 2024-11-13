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
 * @author karthik on 02/07/22.
 * @project totservices
 */

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @JsonInclude(JsonInclude.Include.NON_NULL)
@Document("tot_report") public class Reports {

    @Id private String reportId;
    private String reportedEntity; //user or post
    private String reportedId; //userId or postId
    private String reportType;
    private String reportDescription;
    private String reportedBy;
    private Timestamp reportTime;

}

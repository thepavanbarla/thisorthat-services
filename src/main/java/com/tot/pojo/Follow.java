package com.tot.pojo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

/**
 * @author karthik on 05/12/21.
 * @project totservices
 */
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @JsonInclude(JsonInclude.Include.NON_NULL)
public class Follow {
    String srcUserId;
    String tgtUserId;
    Boolean accepted;
    Timestamp createdAt;
    Timestamp acceptedAt;
}

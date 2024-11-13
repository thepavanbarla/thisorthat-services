package com.tot.pojo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

/**
 * @author karthik on 15/01/22.
 * @project totservices
 */

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @JsonInclude(JsonInclude.Include.NON_NULL)
public class Session {
    String userId;
    String deviceId;
    String token;
    String refreshToken;
    Timestamp expiry;

    //API for insert session details and get session details by
    //API for interests find and save
}

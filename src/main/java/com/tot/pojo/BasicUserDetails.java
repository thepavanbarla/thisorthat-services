package com.tot.pojo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

/**
 * @author karthik on 19/01/22.
 * @project totservices
 */

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @JsonInclude(JsonInclude.Include.NON_NULL)
public class BasicUserDetails {
	String userId;
    String userName;
    Date dob;
    String fullName;
    String gender;
    String profilePicture;
    String phoneNumber;//for geo analytics
}

package com.tot.pojo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;
import java.sql.Timestamp;

/**
 * @author karthik on 12/08/21.
 * @project tot-services
 */
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @JsonInclude(JsonInclude.Include.NON_NULL)
public class User {

    private String userId; //First Firebase UID
    private String userName; //Required
    private String email; //Optional
    private String password; //Optional or OTP
    private String phoneNumber; //Required
    private String gender; //Required
    private Date dob; //Required
    private String fullName; //Required
    //    private String lastName; //Optional
    private String bio; //Optional
    private String profilePicture; //Optional
    private String coverPicture; //Optional
    private Boolean isPrivate = false; //Optional
    private Timestamp createdAt;
    private Timestamp lastModified;
    private Boolean isActive = true;
    private String website;
    private Boolean isChannel = false;

    public User(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    public User(String userName, String fullName, String profilePicture, String userId) {
        this.userName = userName;
        this.fullName = fullName;
        this.profilePicture = profilePicture;
        this.userId = userId;
    }

    public String toString() {
        try {
            return new ObjectMapper().writeValueAsString(this);
        } catch (JsonProcessingException ex) {

        }
        return null;
    }
}

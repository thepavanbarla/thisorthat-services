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
 * @author karthik on 21/01/22.
 * @project totservices
 */
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @JsonInclude(JsonInclude.Include.NON_NULL)
@Document("notifications") public class Notification implements Cloneable {

    @Id String notificationId;
    String userId;//sendToId
    String senderId;
    String postId;
    String type; //vote, comment, follow, follow_request, follow_accept
    String content;
    Timestamp createdAt;
    Boolean isSeen = false;
    Boolean isValid = true;
    
    public Notification(String userId, String senderId, String postId, String type, String content, Timestamp createdAt) {
    	this.userId = userId;
    	this.senderId = senderId;
    	this.postId = postId;
    	this.type = type;
    	this.content = content;
    	this.createdAt = createdAt;
    }

    @Override public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}

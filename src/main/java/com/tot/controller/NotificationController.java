package com.tot.controller;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.TimeZone;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tot.pojo.Notification;
import com.tot.pojo.RestResponse;
import com.tot.service.NotificationService;
import com.tot.utils.Constants;
import com.tot.utils.UserIdentityUtils;

/**
 * @author karthik on 21/01/22.
 * @project totservices
 */

@RestController public class NotificationController {

    @Autowired NotificationService notificationService;

    TimeZone tz = TimeZone.getTimeZone("UTC");
    DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
    // Quoted "Z" to indicate UTC, no timezone offset
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

    @PutMapping("/notifications/update")
    public ResponseEntity<RestResponse> createNotification(@RequestBody Notification notification) {
        RestResponse response = notificationService.createNotification(notification);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/notifications/invalidate/{notificationId}")
    public ResponseEntity<RestResponse> invalidateNotification(
        @PathVariable String notificationId) {
        RestResponse response = notificationService.invalidateNotification(notificationId);
        return ResponseEntity.ok(response);
    }

    @GetMapping({"/notifications", "/notification/{createdAt}",
        "/notification/{createdAt}/{markSeen}"})
    public ResponseEntity<RestResponse> getNotification(
        @PathVariable(required = false) String createdAt,
        @PathVariable(required = false) Boolean markSeen, 
        @RequestParam(required = false) Integer skip,
        @RequestParam(required = false) Integer limit) {
        String userId = UserIdentityUtils.getUserIdFromSecurityContext();
        Timestamp timestamp;
        if (markSeen == null) {
            markSeen = true;
        }
        if(skip ==  null || limit == null){
            skip = Constants.DEFAULT_SKIP;
            limit = Constants.DEFAULT_LIMIT;
        }
        if (createdAt == null) {
            timestamp = Timestamp.from(Instant.now());
        } else {
            LocalDateTime localDateTime = LocalDateTime.from(formatter.parse(createdAt));
            timestamp = Timestamp.valueOf(localDateTime);
        }
        
        RestResponse response =
            notificationService.getNotifications(userId, timestamp, markSeen, skip, limit);
        return ResponseEntity.ok(response);
    }

}

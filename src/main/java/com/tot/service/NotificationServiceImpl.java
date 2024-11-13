package com.tot.service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.tot.dao.NotificationDao;
import com.tot.pojo.Notification;
import com.tot.pojo.RestResponse;

import lombok.extern.slf4j.Slf4j;

/**
 * @author karthik on 21/01/22.
 * @project totservices
 */

@Slf4j @Service public class NotificationServiceImpl implements NotificationService {

    @Autowired NotificationDao notificationDao;

    @Override public RestResponse createNotification(Notification notification) {
        Timestamp instant = Timestamp.from(Instant.now());
        notification.setCreatedAt(instant);
        Notification response = notificationDao.save(notification);
        return RestResponse.getSuccessResponse(response.getNotificationId());
    }

    @Override
    public RestResponse getNotifications(String userId, Timestamp createdAt, Boolean markSeen,
        int skip, int limit) {
        List<Notification> response = notificationDao
            .findByUserIdAndCreatedAtGreaterThanEqualAndIsSeenAndIsValid(userId, createdAt, false,
                true, PageRequest.of(skip, limit), Sort.by(Sort.Direction.DESC, "createdAt"));
        if (markSeen) {
            CompletableFuture.runAsync(() -> {
                deleteNotifications(response);
            });
        }
        return RestResponse.getSuccessResponse(response);
    }

    @Override public RestResponse invalidateNotification(String notificationId) {
        Optional<Notification> result = notificationDao.findById(notificationId);
        if (result.isPresent()) {
            Notification notification = result.get();
            notification.setIsValid(false);
            notificationDao.save(notification);
        }
        return RestResponse.getSuccessResponse("success");
    }

    public void deleteNotifications(List<Notification> notifications) {
        for (Notification notification : notifications) {
            Notification clone = null;
            try {
                clone = (Notification) notification.clone();
            } catch (CloneNotSupportedException e) {
                log.error("Cannot clone notification object: " + notification.getNotificationId(),
                    e);
            }
            clone.setIsSeen(true);
            notificationDao.save(clone);
        }
    }
}

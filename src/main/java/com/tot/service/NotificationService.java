package com.tot.service;

import com.tot.pojo.Notification;
import com.tot.pojo.RestResponse;

import java.sql.Timestamp;

public interface NotificationService {
    RestResponse createNotification(Notification notification);

    RestResponse getNotifications(String userId, Timestamp createdAt, Boolean markSeenAsDeleted,
        int skip, int limit);

    RestResponse invalidateNotification(String notificationId);
}

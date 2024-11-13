package com.tot.dao;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.tot.pojo.Notification;

/**
 * @author karthik on 21/01/22.
 * @project totservices
 */

@Repository public interface NotificationDao extends MongoRepository<Notification, String> {

    List<Notification> findByUserIdAndCreatedAtGreaterThanEqualAndIsSeenAndIsValid(String userId,
        Timestamp createdAt, Boolean isSeen, Boolean isValid, PageRequest page, Sort sort);

    void deleteByUserIdAndSenderIdAndType(String userId, String senderId, String type);
}

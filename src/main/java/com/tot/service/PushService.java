package com.tot.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.firebase.messaging.BatchResponse;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.MulticastMessage;
import com.google.firebase.messaging.Notification;
import com.tot.dao.UserDao;
import com.tot.pojo.UserPreference;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class PushService {
	
	@Autowired
	UserDao userDao;
	
	public void sendProfilePushNotificationToUser(String userId, String title, String body, String targetUserName) {
		UserPreference userPreference = userDao.getUserPreference(userId);
		if(userPreference.getAllowFollowNotifications()) {
			Map<String, String> data = new HashMap<>();
			data.put("type", "Profile");
			data.put("userName", targetUserName);
			sendPushNotificationToUser(userId, title, body, data);
		}
	}
	
	public void sendPostPushNotificationToUser(String userId, String title, String body, String postId) {
		UserPreference userPreference = userDao.getUserPreference(userId);
		if(userPreference.getAllowPostNotifications()) {
			Map<String, String> data = new HashMap<>();
			data.put("type", "Post");
			data.put("postId", postId);
			sendPushNotificationToUser(userId, title, body, data);
		}
	}
	
	private void sendPushNotificationToUser(String userId, String title, String body, Map<String, String> data) {
		List<String> registrationTokens = userDao.getUserDevices(userId);
		try {
			sendPushNotificationToListOfDevices(registrationTokens, title, body, data);
		} catch (FirebaseMessagingException e) {
			log.error("Could not send notification", e);
		}
	}
		
	private void sendPushNotificationToListOfDevices(List<String> registrationTokens, String title, String body, Map<String, String> data) throws FirebaseMessagingException {
		MulticastMessage message = MulticastMessage.builder()
			.setNotification(Notification.builder()
				.setTitle(title)
				.setBody(body)
				.build())
			.putAllData(data)
		    .addAllTokens(registrationTokens)
		    .build();
		BatchResponse response = FirebaseMessaging.getInstance().sendMulticast(message);
		log.info("{} messages were sent successfully", response.getSuccessCount());
	}
	
}

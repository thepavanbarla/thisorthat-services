package com.tot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tot.dao.InterestsDao;
import com.tot.dao.UserDao;
import com.tot.pojo.Interests;
import com.tot.pojo.RestResponse;
import com.tot.utils.UserIdentityUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * @author karthik on 15/01/22.
 * @project totservices
 */

@Slf4j
@Service public class InterestsServiceImpl implements InterestService {

    @Autowired InterestsDao interestsDao;
    @Autowired UserDao userDao;

    @Override public RestResponse getInterests() {
    	String userId = UserIdentityUtils.getUserIdFromSecurityContext();
    	Interests interests = interestsDao.findByUserId(userId);
    	try {
			log.info(new ObjectMapper().writeValueAsString(interests));
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return RestResponse.getSuccessResponse(userDao.getUserInterests(interests.getInterests()));
    }

    @Override public RestResponse saveInterests(Interests interests) {
        return RestResponse.getSuccessResponse(interestsDao.save(interests));
    }
}

package com.tot.service;

import com.tot.pojo.Interests;
import com.tot.pojo.RestResponse;

/**
 * @author karthik on 15/01/22.
 * @project totservices
 */

public interface InterestService {
    RestResponse getInterests();
    RestResponse saveInterests(Interests interests);
}

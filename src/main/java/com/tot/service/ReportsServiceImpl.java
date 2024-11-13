package com.tot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tot.dao.ReportsDao;
import com.tot.pojo.Reports;
import com.tot.pojo.RestResponse;

/**
 * @author karthik on 02/07/22.
 * @project totservices
 */
@Service
public class ReportsServiceImpl implements ReportsService {

    @Autowired ReportsDao reportsDao;

    @Override public RestResponse saveReport(Reports report) {
        Reports result = reportsDao.save(report);
        return RestResponse.getSuccessResponse(result.getReportId());
    }

    @Override public RestResponse getReportsByPostId(String postId, Integer skip, Integer limit) {
        return null;
    }

    @Override public RestResponse getReportsByUserId(String userId, Integer skip, Integer limit) {
        return null;
    }
}

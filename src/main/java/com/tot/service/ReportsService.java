package com.tot.service;

import com.tot.pojo.Reports;
import com.tot.pojo.RestResponse;

public interface ReportsService {

    RestResponse saveReport(Reports report);

    RestResponse getReportsByPostId(String postId, Integer skip, Integer limit);

    RestResponse getReportsByUserId(String userId, Integer skip, Integer limit);
}

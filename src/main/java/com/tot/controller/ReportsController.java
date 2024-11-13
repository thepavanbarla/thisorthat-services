package com.tot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.tot.pojo.Reports;
import com.tot.pojo.RestResponse;
import com.tot.service.ReportsService;
import com.tot.utils.Constants;

/**
 * @author karthik on 02/07/22.
 * @project totservices
 */

@RestController public class ReportsController {

    @Autowired ReportsService reportsService;

    @PutMapping("/report/user/save")
    public ResponseEntity<RestResponse> saveUserReport(@RequestBody Reports report) {
        report.setReportedEntity("user");
        RestResponse response = reportsService.saveReport(report);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/report/post/save")
    public ResponseEntity<RestResponse> savePostReport(@RequestBody Reports report) {
        report.setReportedEntity("post");
        RestResponse response = reportsService.saveReport(report);
        return ResponseEntity.ok(response);
    }

    @GetMapping({"/report/post/{postId}", "/report/post/{postId}/{skip}/{limit}"})
    public ResponseEntity<RestResponse> getVotesByPostId(@PathVariable String postId,
        @PathVariable(required = false) Integer skip,
        @PathVariable(required = false) Integer limit) {
        if (skip == null || limit == null) {
            skip = Constants.DEFAULT_SKIP;
            limit = Constants.DEFAULT_LIMIT;
        }
        RestResponse response = reportsService.getReportsByPostId(postId, skip, limit);
        return ResponseEntity.ok(response);
    }

    @GetMapping({"/report/user/{userId}", "/report/user/{userId}/{skip}/{limit}"})
    public ResponseEntity<RestResponse> getVotesByUserId(@PathVariable String userId,
        @PathVariable(required = false) Integer skip,
        @PathVariable(required = false) Integer limit) {
        if (skip == null || limit == null) {
            skip = Constants.DEFAULT_SKIP;
            limit = Constants.DEFAULT_LIMIT;
        }
        RestResponse response = reportsService.getReportsByUserId(userId, skip, limit);
        return ResponseEntity.ok(response);
    }

}

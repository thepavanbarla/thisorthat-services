package com.tot.controller;

import com.tot.pojo.RestResponse;
import com.tot.utils.Constants;
import com.tot.utils.GoogleImageScraper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author karthik on 13/12/22.
 * @project totservices
 */

@RestController @Slf4j public class GoogleImageScraperController {

    //get image urls from google based on search query
    @GetMapping("/image/urls") public ResponseEntity<RestResponse> getImageUrls(
        @RequestParam String keywords, @RequestParam(required = false) Integer limit) {
        List<String> urlList;
        RestResponse response;
        if (limit == null) {
            limit = Constants.DEFAULT_URL_LIMIT;
        }
        try {
            urlList = GoogleImageScraper.getImagesFromGoogle(keywords, limit);
            response = RestResponse.getSuccessResponse(urlList);
        } catch (Exception e) {
            log.error("Failed to get images for keywords: " + keywords, e);
            response = RestResponse
                .getFailedResponse(403, "Cannot get results at the moment: " + e.getMessage());
        }
        return ResponseEntity.ok(response);
    }

}

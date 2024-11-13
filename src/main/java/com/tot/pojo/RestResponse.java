package com.tot.pojo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author karthik on 17/08/21.
 * @project tot-services
 */

@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class RestResponse {
    Boolean isError;
    List<Error> errors;
    Object data;

    public static RestResponse getSuccessResponse(Object data) {
        return new RestResponse(false, new ArrayList<>(), data);
    }

    public static RestResponse getFailedResponse(Integer errorCode, String errorMessage) {
        return new RestResponse(true,
            new ArrayList<>(Arrays.asList(new Error(errorCode, errorMessage))), null);
    }

    public void addError(Integer errorCode, String errorMessage) {
        addError(new Error(errorCode, errorMessage));
    }
    
    public String convertToJson() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(this);
    }

    public void addError(Error error) {
        if (errors == null)
            errors = new ArrayList<>();
        errors.add(error);
    }

}


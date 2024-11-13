package com.tot.pojo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author karthik on 17/08/21.
 * @project tot-services
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Error {
    private Integer code;
    private String message;
}

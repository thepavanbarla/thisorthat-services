package com.tot.pojo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.relational.core.mapping.Table;

/**
 * @author karthik on 04/06/22.
 * @project totservices
 */
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @JsonInclude(JsonInclude.Include.NON_NULL)
@Table("USER_PREFERENCE") public class UserPreference {
    private String userId;
    private Boolean allowNotifications;
    private Boolean allowPostNotifications;
    private Boolean allowFollowNotifications;

}

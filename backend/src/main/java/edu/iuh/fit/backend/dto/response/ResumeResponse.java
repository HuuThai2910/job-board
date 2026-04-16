/*
 * @ (#) .java    1.0
 * Copyright (c)  IUH. All rights reserved.
 */
package edu.iuh.fit.backend.dto.response;

import edu.iuh.fit.backend.util.constant.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

/*
 * @description
 * @author: Huu Thai
 * @date:
 * @version: 1.0
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResumeResponse {
    private long id;
    private String email;
    private String url;
    private Status status;
    private Instant createdAt;
    private Instant updatedAt;
    private String createdBy;
    private String updatedBy;
    private String companyName;
    private UserSummaryResponse user;
    private JobSummaryResponse job;
}

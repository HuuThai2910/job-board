/*
 * @ (#) .java    1.0
 * Copyright (c)  IUH. All rights reserved.
 */
package edu.iuh.fit.backend.dto.response;

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
@NoArgsConstructor
@AllArgsConstructor
public class UploadFileResponse {
    private String fileName;
    private Instant uploadedAt;
}

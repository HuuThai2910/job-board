/*
 * @ (#) .java    1.0
 * Copyright (c)  IUH. All rights reserved.
 */
package edu.iuh.fit.backend.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

/*
 * @description
 * @author: Huu Thai
 * @date:
 * @version: 1.0
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
public class ApiResponse<T> {
    private int statusCode;
    private Object message;
    private T data;
    private String errorCode;
    private LocalDateTime timestamp = LocalDateTime.now();

    public ApiResponse(int statusCode, Object message, T data, String errorCode) {
        this.statusCode = statusCode;
        this.message = message;
        this.data = data;
        this.errorCode = errorCode;
        this.timestamp = LocalDateTime.now();
    }


}

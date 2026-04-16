/*
 * @ (#) .java    1.0
 * Copyright (c)  IUH. All rights reserved.
 */
package edu.iuh.fit.backend.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

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
@ToString
public class LoginRequest {
    @NotBlank(message = "User name cannot be blank")
    private String username;
    @NotBlank(message = "Password cannot be blank")
    private String password;
}

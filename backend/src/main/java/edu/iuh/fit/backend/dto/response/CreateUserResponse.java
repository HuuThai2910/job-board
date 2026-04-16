/*
 * @ (#) .java    1.0
 * Copyright (c)  IUH. All rights reserved.
 */
package edu.iuh.fit.backend.dto.response;

import edu.iuh.fit.backend.util.constant.Gender;
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
public class CreateUserResponse {
    private Long id;
    private String name;
    private String email;
    private int age;
    private Gender gender;
    private String address;
//    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss a", timezone = "GMT+7")
    private Instant createdAt;

    private CompanySummaryResponse company;
}

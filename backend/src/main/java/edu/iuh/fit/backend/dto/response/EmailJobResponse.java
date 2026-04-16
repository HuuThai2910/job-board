/*
 * @ (#) .java    1.0
 * Copyright (c)  IUH. All rights reserved.
 */
package edu.iuh.fit.backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/*
 * @description
 * @author: Huu Thai
 * @date:
 * @version: 1.0
 */
@Getter
@Setter
public class EmailJobResponse {
    private String name;
    private double salary;
    private CompanyEmail company;
    private List<SkillEmail> skills;
    @Getter
    @Setter
    @AllArgsConstructor
    public static class CompanyEmail {
        private String name;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class SkillEmail {
        private String name;
    }

}

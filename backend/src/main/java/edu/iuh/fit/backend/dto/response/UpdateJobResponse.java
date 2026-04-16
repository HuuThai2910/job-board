/*
 * @ (#) .java    1.0
 * Copyright (c)  IUH. All rights reserved.
 */
package edu.iuh.fit.backend.dto.response;

import edu.iuh.fit.backend.util.constant.Level;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.List;

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
public class UpdateJobResponse {
    private Long id;
    private String name;
    private String location;
    private double salary;
    private int quantity;
    private Level level;
    private String description;
    private Instant startDate;
    private Instant endDate;
    private boolean active;
    private Instant updatedAt;
    private String updatedBy;
    private List<String> skills;
}

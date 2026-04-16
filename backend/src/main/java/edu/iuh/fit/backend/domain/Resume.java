/*
 * @ (#) .java    1.0
 * Copyright (c)  IUH. All rights reserved.
 */
package edu.iuh.fit.backend.domain;

/*
 * @description
 * @author: Huu Thai
 * @date:
 * @version: 1.0
 */

import com.fasterxml.jackson.annotation.JsonIgnore;
import edu.iuh.fit.backend.util.SecurityUtil;
import edu.iuh.fit.backend.util.constant.Status;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Table(name = "resumes")
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Resume {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String email;
    private String url;
    @Enumerated(EnumType.STRING)
    private Status status;
    private Instant createdAt;
    private Instant updatedAt;
    private String createdBy;
    private String updatedBy;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "job_id")
    private Job job;

    @PrePersist
    void handleBeforeCreate(){
        createdBy = SecurityUtil.getCurrentUserLogin().isPresent()
                ? SecurityUtil.getCurrentUserLogin().get()
                : "";
        createdAt = Instant.now();
    }
    @PreUpdate
    void handleBeforeUpdate(){
        updatedBy = SecurityUtil.getCurrentUserLogin().isPresent()
                ? SecurityUtil.getCurrentUserLogin().get()
                : "";
        updatedAt = Instant.now();
    }
}

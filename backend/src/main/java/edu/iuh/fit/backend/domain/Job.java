/*
 * @ (#) .java    1.0
 * Copyright (c)  IUH. All rights reserved.
 */
package edu.iuh.fit.backend.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import edu.iuh.fit.backend.util.SecurityUtil;
import edu.iuh.fit.backend.util.constant.Level;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.List;

/*
 * @description
 * @author: Huu Thai
 * @date:
 * @version: 1.0
 */
@Table(name = "jobs")
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Job {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String location;
    private double salary;
    private int quantity;
    @Enumerated(EnumType.STRING)
    private Level level;
    @Column(columnDefinition = "MEDIUMTEXT")
    private String description;
    private Instant startDate;
    private Instant endDate;
    private boolean active;
    private Instant createdAt;
    private Instant updatedAt;
    private String createdBy;
    private  String updatedBy;

    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;

    @OneToMany(mappedBy = "job")
    @JsonIgnore
    private List<Resume> resumes;

    @ToString.Exclude
    @ManyToMany
    @JsonIgnoreProperties(value = "jobs")
    @JoinTable(name = "job_skill", joinColumns = @JoinColumn(name = "job_id"),
            inverseJoinColumns = @JoinColumn(name = "skill_id"))
    private List<Skill> skills;

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

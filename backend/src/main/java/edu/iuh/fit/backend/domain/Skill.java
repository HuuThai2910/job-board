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
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.Instant;
import java.util.List;

/*
 * @description
 * @author: Huu Thai
 * @date:
 * @version: 1.0
 */
@Table(name = "skills")
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Skill {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank(message = "Name cannot be blank")
    private String name;
    private Instant createdAt;
    private Instant updatedAt;
    private String createdBy;
    private  String updatedBy;

    @ToString.Exclude
    @ManyToMany(mappedBy = "skills")
    @JsonIgnore
    private List<Job> jobs;

    @ManyToMany(mappedBy = "skills")
    @JsonIgnore
    private List<Subscriber> subscribers;

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

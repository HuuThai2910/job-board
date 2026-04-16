/*
 * @ (#) .java    1.0
 * Copyright (c)  IUH. All rights reserved.
 */
package edu.iuh.fit.backend.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
@Table(name = "permissions")
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Permission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String apiPath;
    private String method;
    private String module;
    private Instant createdAt;
    private Instant updatedAt;
    private String createdBy;
    private  String updatedBy;

    @ManyToMany(mappedBy = "permissions")
    @JsonIgnore
    private List<Role> roles;

    public Permission(String name, String apiPath, String method, String moudle){
        this.name = name;
        this.apiPath = apiPath;
        this.method = method;
        this.module = moudle;
    }

    @PrePersist
    void handleBeforeCreate(){
        this.createdBy = SecurityUtil.getCurrentUserLogin().isPresent()
                ? SecurityUtil.getCurrentUserLogin().get()
                : "";
        this.createdAt = Instant.now();
    }

    @PreUpdate
    public void handleBeforeUpdate(){
        this.updatedBy = SecurityUtil.getCurrentUserLogin().isPresent()
                ? SecurityUtil.getCurrentUserLogin().get()
                : "";
        this.updatedAt = Instant.now();
    }
}

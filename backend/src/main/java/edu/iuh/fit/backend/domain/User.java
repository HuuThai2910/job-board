/*
 * @ (#) .java    1.0
 * Copyright (c)  IUH. All rights reserved.
 */
package edu.iuh.fit.backend.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import edu.iuh.fit.backend.util.SecurityUtil;
import edu.iuh.fit.backend.util.constant.Gender;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.time.Instant;
import java.util.List;

/*
 * @description
 * @author: Huu Thai
 * @date:
 * @version: 1.0
 */
@Entity
@Table(name = "`users`")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name cannot be blank")
    private String name;

    @Email(message = "Email is not valid", regexp = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$")
    @NotEmpty(message = "Email cannot be empty")
    private String email;

    private String password;

    private int age;
    @Enumerated(EnumType.STRING)
    private Gender gender;
    private String address;
    @Column(columnDefinition = "MEDIUMTEXT")
    private String refreshToken;
    private Instant createdAt;
    private Instant updatedAt;
    private String createBy;
    private String updatedBy;

    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private List<Resume> resumes;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;

//    Ham them nguoi tao user truoc khi save user
    @PrePersist
    public void handleBeforeCreate(){
        this.createBy = SecurityUtil.getCurrentUserLogin().isPresent()
                ? SecurityUtil.getCurrentUserLogin().get()
                : "";
        this.createdAt = Instant.now();
    }

//    Ham them nguoi cap nhat user
    @PreUpdate
    public void handleBeforeUpdate(){
        this.updatedBy = SecurityUtil.getCurrentUserLogin().isPresent()
                ? SecurityUtil.getCurrentUserLogin().get()
                : "";
        this.updatedAt = Instant.now();
    }

}

/*
 * @ (#) .java    1.0
 * Copyright (c)  IUH. All rights reserved.
 */
package edu.iuh.fit.backend.controller;

import com.turkraft.springfilter.boot.Filter;
import edu.iuh.fit.backend.dto.response.CreateUserResponse;
import edu.iuh.fit.backend.dto.response.UpdateUserResponse;
import edu.iuh.fit.backend.dto.response.UserResponse;
import edu.iuh.fit.backend.dto.ResultPaginationDTO;
import edu.iuh.fit.backend.dto.response.ApiResponse;
import edu.iuh.fit.backend.domain.User;
import edu.iuh.fit.backend.service.UserService;
import edu.iuh.fit.backend.service.impl.UserServiceImpl;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

/*
 * @description
 * @author: Huu Thai
 * @date:
 * @version: 1.0
 */
@RestController
@RequestMapping("/api/v1")
public class UserController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public UserController(UserServiceImpl userServiceImpl, PasswordEncoder passwordEncoder) {
        this.userService = userServiceImpl;
        this.passwordEncoder = passwordEncoder;

    }

    @PostMapping("/users")
    public ResponseEntity<ApiResponse<CreateUserResponse>> createUser(@RequestBody @Valid User user) {
        String hashPassword = this.passwordEncoder.encode(user.getPassword());
        user.setPassword(hashPassword);
        CreateUserResponse dto = this.userService.createUser(user);
        var result = new ApiResponse<>(HttpStatus.CREATED.value(), "createUser", dto, null);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @GetMapping("/users")
    public ResponseEntity<ApiResponse<ResultPaginationDTO>> getAllUsers(@Filter Specification<User> specification, Pageable pageable) {
        ResultPaginationDTO resultPaginationDTO = this.userService.getAllUsers(specification, pageable);
        var result = new ApiResponse<>(HttpStatus.OK.value(), "getAllUsers", resultPaginationDTO, null);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<ApiResponse<UserResponse>> getUserById(@PathVariable Long id) {
        UserResponse userResponse = this.userService.getUserById(id);
        var response = new ApiResponse<>(HttpStatus.OK.value(), "getUserById", userResponse, null);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/users")
    public ResponseEntity<ApiResponse<UpdateUserResponse>> updateUser(@RequestBody User user) {
        UpdateUserResponse updated = userService.updateUser(user);
        var result = new ApiResponse<>(HttpStatus.CREATED.value(), "updateUser", updated, null);
        return ResponseEntity.ok(result);
    }
    @DeleteMapping("/users/{id}")
    public ResponseEntity<ApiResponse<String>> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        var result = new ApiResponse<>(HttpStatus.OK.value(), "deleteUser", "delete successfully", null);
        return ResponseEntity.ok(result);
    }
}

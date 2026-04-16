/*
 * @ (#) .java    1.0
 * Copyright (c)  IUH. All rights reserved.
 */
package edu.iuh.fit.backend.service;

import edu.iuh.fit.backend.domain.Company;
import edu.iuh.fit.backend.domain.User;
import edu.iuh.fit.backend.dto.response.CreateUserResponse;
import edu.iuh.fit.backend.dto.response.UpdateUserResponse;
import edu.iuh.fit.backend.dto.response.UserResponse;
import edu.iuh.fit.backend.dto.ResultPaginationDTO;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

/*
 * @description
 * @author: Huu Thai
 * @date:
 * @version: 1.0
 */
public interface UserService {

    CreateUserResponse createUser(User user);

    ResultPaginationDTO getAllUsers(Specification<User> specification, Pageable pageable);

    UserResponse getUserById(Long id);

    UpdateUserResponse updateUser(User updatedUser);

    void deleteUser(Long id);

    User getUserByUserName(String userName);


    void updateUserToken(String token, String email);

    User getUserByRefreshTokenAndEmail(String token, String email);
}
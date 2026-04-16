/*
 * @ (#) .java    1.0
 * Copyright (c)  IUH. All rights reserved.
 */
package edu.iuh.fit.backend.service.impl;

import edu.iuh.fit.backend.domain.Company;
import edu.iuh.fit.backend.domain.Role;
import edu.iuh.fit.backend.domain.User;
import edu.iuh.fit.backend.dto.*;
import edu.iuh.fit.backend.dto.response.CreateUserResponse;
import edu.iuh.fit.backend.dto.response.UpdateUserResponse;
import edu.iuh.fit.backend.dto.response.UserResponse;
import edu.iuh.fit.backend.mapper.UserMapper;
import edu.iuh.fit.backend.repository.CompanyRepository;
import edu.iuh.fit.backend.repository.UserRepository;
import edu.iuh.fit.backend.service.RoleService;
import edu.iuh.fit.backend.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

/*
 * @description
 * @author: Huu Thai
 * @date:
 * @version: 1.0
 */
@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;
    private final UserMapper userMapper;
    private final RoleService roleService;

    @Override
    public CreateUserResponse createUser(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }
        if(user.getCompany() != null){
            Company companyExists = companyRepository.findById(user.getCompany().getId()).orElse(null);
            user.setCompany(companyExists);
        }


        if(user.getRole() != null){
            Role r = this.roleService.fetchById(user.getRole().getId());
            user.setRole(r);
        }
        User newUser = this.userRepository.save(user);
        return this.userMapper.toResCreateUserDTO(newUser);
    }

    @Override
    public ResultPaginationDTO getAllUsers(Specification<User> specification, Pageable pageable) {
        Page<User> companyPage =  userRepository.findAll(specification, pageable);
        ResultPaginationDTO rs = new ResultPaginationDTO();
        Meta meta = new Meta();
        meta.setPage(pageable.getPageNumber() + 1);
        meta.setPageSize(pageable.getPageSize());
        meta.setPages(companyPage.getTotalPages());
        meta.setTotal(companyPage.getTotalElements());
        rs.setMeta(meta);
        rs.setResult(this.userMapper.toResListUserDTO(companyPage.getContent()));
        return rs;
    }

    @Override
    public UserResponse getUserById(Long id) {
        User user = this.userRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("User not found"));
        return this.userMapper.toResUserDTO(user);
    }

    @Override
    public UpdateUserResponse updateUser(User updatedUser) {
        return this.userRepository.findById(updatedUser.getId()).map(user -> {
            user.setName(updatedUser.getName());
            user.setGender(updatedUser.getGender());
            user.setAddress(updatedUser.getAddress());
            user.setAge(updatedUser.getAge());
            user.setEmail(updatedUser.getEmail());
            if(updatedUser.getCompany() != null){
                Company companyExists = this.companyRepository.findById(updatedUser.getCompany().getId()).orElse(null);
                user.setCompany(companyExists);
            }
            if(updatedUser.getRole() != null) {
                Role r = this.roleService.fetchById(updatedUser.getRole().getId());
                user.setRole(r);
            }
            User currentUser = this.userRepository.save(user);
            return this.userMapper.toResUpdateUserDTO(currentUser);
        }).orElseThrow(() -> new NoSuchElementException("User not found"));
    }

    @Override
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new NoSuchElementException("User not found");
        }
        userRepository.deleteById(id);
    }

    @Override
    public User getUserByUserName(String userName) {
        return this.userRepository.findByEmail(userName);
    }

    @Override
    public void updateUserToken(String token, String email) {
        User currentUser = this.getUserByUserName(email);
        if(currentUser != null){
            currentUser.setRefreshToken(token);
            this.userRepository.save(currentUser);
        }
    }
    @Override
    public User getUserByRefreshTokenAndEmail(String token, String email){
        return this.userRepository.findByRefreshTokenAndEmail(token, email);
    }


}

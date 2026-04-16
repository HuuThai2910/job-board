/*
 * @ (#) .java    1.0
 * Copyright (c)  IUH. All rights reserved.
 */
package edu.iuh.fit.backend.controller;

import edu.iuh.fit.backend.domain.User;
import edu.iuh.fit.backend.dto.request.LoginRequest;
import edu.iuh.fit.backend.dto.response.CreateUserResponse;
import edu.iuh.fit.backend.dto.response.LoginResponse;
import edu.iuh.fit.backend.service.UserService;
import edu.iuh.fit.backend.util.SecurityUtil;
import edu.iuh.fit.backend.util.annotaion.ApiMessage;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

/*
 * @description
 * @author: Huu Thai
 * @date:
 * @version: 1.0
 */
@RestController
@RequestMapping("/api/v1")
public class AuthController {
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final SecurityUtil securityUtil;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    @Value("${thai.jwt.refresh-token-validity-in-seconds}")
    private Long refreshTokenExpiration;

    public AuthController(AuthenticationManagerBuilder authenticationManagerBuilder, SecurityUtil securityUtil, UserService userService, PasswordEncoder passwordEncoder) {
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.securityUtil = securityUtil;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/auth/login")
    @ApiMessage("Login successfully")
    public ResponseEntity<LoginResponse> login(@RequestBody @Valid LoginRequest loginRequest) {
        //Nạp input gồm username/password vào Security
        UsernamePasswordAuthenticationToken authenticationToken
                = new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword());

        //xác thực người dùng => cần viết hàm loadUserByUsername
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
//        Set thong tin nguoi dung dang nhap vao context
        SecurityContextHolder.getContext().setAuthentication(authentication);

        LoginResponse res = new LoginResponse();
        User currentUser = this.userService.getUserByUserName(loginRequest.getUsername());
        LoginResponse.UserLogin userLogin = new LoginResponse.UserLogin(
                currentUser.getId(),
                currentUser.getEmail(),
                currentUser.getName(),
                currentUser.getRole());
        res.setUser(userLogin);
//        Create a token
        String access_token = this.securityUtil.createAccessToken(authentication.getName(), res);
        res.setAccessToken(access_token);

//        Create refresh token
        String refresh_token = this.securityUtil.createRefreshToken(loginRequest.getUsername(), res);
//        Update user
        this.userService.updateUserToken(refresh_token, loginRequest.getUsername());

//        Set cookies
        ResponseCookie responseCookie = ResponseCookie.from("refresh_token", refresh_token)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(refreshTokenExpiration)
                .build();
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, responseCookie.toString())
                .body(res);
    }

    @GetMapping("/auth/account")
    @ApiMessage("fetch account")
    public ResponseEntity<LoginResponse.UserGetAccount> getAccount() {
        String email = SecurityUtil.getCurrentUserLogin().isPresent()
                ? SecurityUtil.getCurrentUserLogin().get()
                : "";
        User currentUser = this.userService.getUserByUserName(email);
        LoginResponse.UserLogin userLogin = new LoginResponse.UserLogin(
                currentUser.getId(),
                currentUser.getEmail(),
                currentUser.getName(),
                currentUser.getRole()
        );
        LoginResponse.UserGetAccount userGetAccount = new LoginResponse.UserGetAccount(userLogin);
        return ResponseEntity.ok().body(userGetAccount);
    }

    @GetMapping("/auth/refresh")
    @ApiMessage("Get user by refresh token")
    public ResponseEntity<LoginResponse> getRefreshToken(@CookieValue(name = "refresh_token") String refresh_token) {

//        Check valid
        Jwt decodedToken = this.securityUtil.checkValidRefreshToken(refresh_token);
        String email = decodedToken.getSubject();
//        Check user by token + email
        User currentUser = this.userService.getUserByRefreshTokenAndEmail(refresh_token, email);
        if (currentUser == null) {
            throw new RuntimeException("Refresh token invalid");
        }
        LoginResponse res = new LoginResponse();
        LoginResponse.UserLogin userLogin = new LoginResponse.UserLogin(
                currentUser.getId(),
                currentUser.getEmail(),
                currentUser.getName(),
                currentUser.getRole());
        res.setUser(userLogin);
        //        Create a token
        String access_token = this.securityUtil.createAccessToken(email, res);
        res.setAccessToken(access_token);

//        Create refresh token
        String new_refresh_token = this.securityUtil.createRefreshToken(email, res);
//        Update user
        this.userService.updateUserToken(new_refresh_token, email);

//        Set cookies
        ResponseCookie responseCookie = ResponseCookie.from("refresh_token", new_refresh_token)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(refreshTokenExpiration)
                .build();
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, responseCookie.toString())
                .body(res);
    }
    @PostMapping("/auth/logout")
    @ApiMessage("Logout successfully")
    public ResponseEntity<Void> getLogout(){
        String email = SecurityUtil.getCurrentUserLogin().isPresent()
                ? SecurityUtil.getCurrentUserLogin().get()
                : "";

        this.userService.updateUserToken(null, email);
        ResponseCookie deleteCookie = ResponseCookie
                .from("refresh_token", null)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(0)
                .build();
        return ResponseEntity
                .ok()
                .header(HttpHeaders.SET_COOKIE, deleteCookie.toString())
                .build();

    }

    @PostMapping("/auth/register")
    @ApiMessage("Register a new user")
    public ResponseEntity<CreateUserResponse> register(@Valid @RequestBody User userRegis){
        String hashPassword = this.passwordEncoder.encode(userRegis.getPassword());
        userRegis.setPassword(hashPassword);
        CreateUserResponse newUser = this.userService.createUser(userRegis);
        return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
    }
}

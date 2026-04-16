/*
 * @ (#) .java    1.0
 * Copyright (c)  IUH. All rights reserved.
 */
package edu.iuh.fit.backend.config;

/*
 * @description
 * @author: Huu Thai
 * @date:
 * @version: 1.0
 */

import edu.iuh.fit.backend.domain.Permission;
import edu.iuh.fit.backend.domain.Role;
import edu.iuh.fit.backend.domain.User;
import edu.iuh.fit.backend.service.UserService;
import edu.iuh.fit.backend.util.SecurityUtil;
import edu.iuh.fit.backend.util.error.InvalidException;
import edu.iuh.fit.backend.util.error.PermissionException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;

import java.util.List;


public class PermissionInterceptor implements HandlerInterceptor {
    @Autowired
    UserService userService;

    @Transactional
    @Override
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response, Object handler)
            throws Exception {

        String path = (String) request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE);
        String requestURI = request.getRequestURI();
        String httpMethod = request.getMethod();
        System.out.println(">>> RUN preHandle");
        System.out.println(">>> path= " + path);
        System.out.println(">>> httpMethod= " + httpMethod);
        System.out.println(">>> requestURI= " + requestURI);

//        Check permission
        String email = SecurityUtil.getCurrentUserLogin().isPresent()
                ? SecurityUtil.getCurrentUserLogin().get()
                : "";
        if (!email.isEmpty()) {
            User user = this.userService.getUserByUserName(email);
            if (user != null) {
                Role role = user.getRole();
                if (role != null) {
                    List<Permission> permissions = role.getPermissions();
                    boolean isAllow = permissions.stream().anyMatch(permission ->
                            permission.getApiPath().equals(path) && permission.getMethod().equals(httpMethod)
                    );
                    if(!isAllow){
                        throw new PermissionException("You do not have permission to access this endpoint");
                    }
                }
            }else {
                throw new PermissionException("You do not have permission to access this endpoint");
            }
        }
        return true;
    }
}


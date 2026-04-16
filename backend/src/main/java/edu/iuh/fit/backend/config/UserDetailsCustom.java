/*
 * @ (#) .java    1.0
 * Copyright (c)  IUH. All rights reserved.
 */
package edu.iuh.fit.backend.config;

import edu.iuh.fit.backend.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Collections;

/*
 * @description
 * @author: Huu Thai
 * @date:
 * @version: 1.0
 */
@Component("userDetailsService")
@AllArgsConstructor
public class UserDetailsCustom implements UserDetailsService {
    private final UserService userService;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        edu.iuh.fit.backend.domain.User user = this.userService.getUserByUserName(username);
        if(user == null){
            throw new UsernameNotFoundException("Username / password khong hop le");
        }
        return new User(
                user.getEmail(),
                user.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
        );
    }
}

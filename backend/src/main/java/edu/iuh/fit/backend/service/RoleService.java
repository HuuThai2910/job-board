/*
 * @ (#) .java    1.0
 * Copyright (c)  IUH. All rights reserved.
 */
package edu.iuh.fit.backend.service;

import edu.iuh.fit.backend.domain.Permission;
import edu.iuh.fit.backend.domain.Role;
import edu.iuh.fit.backend.dto.ResultPaginationDTO;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

/*
 * @description
 * @author: Huu Thai
 * @date:
 * @version: 1.0
 */
public interface RoleService {
    boolean existByName(String name);

    Role create(Role r);

    Role fetchById(Long id);

    Role update(Role r);

    void delete(long id);

    ResultPaginationDTO getRoles(Specification<Role> spec, Pageable pageable);
}

/*
 * @ (#) .java    1.0
 * Copyright (c)  IUH. All rights reserved.
 */
package edu.iuh.fit.backend.repository;

import edu.iuh.fit.backend.domain.Resume;
import edu.iuh.fit.backend.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/*
 * @description
 * @author: Huu Thai
 * @date:
 * @version: 1.0
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, Long>, JpaSpecificationExecutor<Role> {
    boolean existsByName(String name);

    Role findByName(String superAdmin);
}
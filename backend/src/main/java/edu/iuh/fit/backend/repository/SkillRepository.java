/*
 * @ (#) .java    1.0
 * Copyright (c)  IUH. All rights reserved.
 */
package edu.iuh.fit.backend.repository;

import edu.iuh.fit.backend.domain.Skill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

/*
 * @description
 * @author: Huu Thai
 * @date:
 * @version: 1.0
 */
@Repository
public interface SkillRepository extends JpaRepository<Skill, Long>, JpaSpecificationExecutor<Skill> {

    boolean existsByNameIgnoreCase(String name);

    List<Skill> findByIdIn(Collection<Long> ids);
}

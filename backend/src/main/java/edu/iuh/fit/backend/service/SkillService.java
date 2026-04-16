/*
 * @ (#) .java    1.0
 * Copyright (c)  IUH. All rights reserved.
 */
package edu.iuh.fit.backend.service;/*
 * @description
 * @author: Huu Thai
 * @date:
 * @version: 1.0
 */

import edu.iuh.fit.backend.domain.Skill;
import edu.iuh.fit.backend.dto.ResultPaginationDTO;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

public interface SkillService {
    Skill handleCreateSkill(Skill skill);

    Skill handleUpdateSkill(Skill skill);

    void handleDeleteSkill(Long id);

    Skill handleGetSkillById(Long id);

    ResultPaginationDTO handleGetAllSkill(Specification<Skill> specification, Pageable pageable);
}

/*
 * @ (#) .java    1.0
 * Copyright (c)  IUH. All rights reserved.
 */
package edu.iuh.fit.backend.controller;

import com.turkraft.springfilter.boot.Filter;
import edu.iuh.fit.backend.domain.Skill;
import edu.iuh.fit.backend.dto.ResultPaginationDTO;
import edu.iuh.fit.backend.service.SkillService;
import edu.iuh.fit.backend.util.annotaion.ApiMessage;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/*
 * @description
 * @author: Huu Thai
 * @date:
 * @version: 1.0
 */
@RestController
@AllArgsConstructor
@RequestMapping("/api/v1")
public class SkillController {
    private final SkillService skillService;

    @PostMapping("/skills")
    @ApiMessage("Create skill successfully")
    public ResponseEntity<Skill> createSkill(@RequestBody @Valid Skill skill){
        Skill newSkill = this.skillService.handleCreateSkill(skill);
        return ResponseEntity.status(HttpStatus.CREATED).body(newSkill);
    }

    @PutMapping("/skills")
    @ApiMessage("Update skill successfully")
    public ResponseEntity<Skill> updateSkill(@RequestBody @Valid Skill skill){
        Skill updatedSkill = this.skillService.handleUpdateSkill(skill);
        return ResponseEntity.ok(updatedSkill);
    }

    @DeleteMapping("/skills/{id}")
    @ApiMessage("Delete skill successfully")
    public ResponseEntity<Void> deleteSkill(@PathVariable Long id){
        this.skillService.handleDeleteSkill(id);
        return ResponseEntity.ok(null);
    }

    @GetMapping("/skills")
    @ApiMessage("Fetch all skills successfully")
    public ResponseEntity<ResultPaginationDTO> getAllSkills(@Filter Specification<Skill> specification, Pageable pageable){
        return ResponseEntity.ok(this.skillService.handleGetAllSkill(specification, pageable));
    }

    @GetMapping("/skills/{id}")
    @ApiMessage("Fetch skill successfully")
    public ResponseEntity<Skill> getSkillById(@PathVariable Long id){
        return ResponseEntity.ok(this.skillService.handleGetSkillById(id));
    }
}

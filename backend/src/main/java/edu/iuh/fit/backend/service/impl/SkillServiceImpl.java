/*
 * @ (#) .java    1.0
 * Copyright (c)  IUH. All rights reserved.
 */
package edu.iuh.fit.backend.service.impl;

import edu.iuh.fit.backend.domain.Skill;
import edu.iuh.fit.backend.dto.Meta;
import edu.iuh.fit.backend.dto.ResultPaginationDTO;
import edu.iuh.fit.backend.repository.SkillRepository;
import edu.iuh.fit.backend.service.SkillService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;

/*
 * @description
 * @author: Huu Thai
 * @date:
 * @version: 1.0
 */
@Service
@AllArgsConstructor
public class SkillServiceImpl implements SkillService {
    private final SkillRepository skillRepository;

    @Override
    public Skill handleCreateSkill(Skill skill){
        if(this.skillRepository.existsByNameIgnoreCase(skill.getName())){
            throw new IllegalArgumentException("Skill already exists");
        }
        return this.skillRepository.save(skill);
    }
    @Override
    public Skill handleUpdateSkill(Skill skill){
         return this.skillRepository.findById(skill.getId())
                .map(skill1 -> {
                    skill1.setName(skill.getName());
                    return this.skillRepository.save(skill1);
                }).orElseThrow(() -> new NoSuchElementException("Skill not found"));
    }
    @Override
    public void handleDeleteSkill(Long id){
        Optional<Skill> skillOptional = this.skillRepository.findById(id);
        if(skillOptional.isEmpty()){
            throw new NoSuchElementException("Skill not found");
        }
        Skill currentSkill = skillOptional.get();
        currentSkill.getJobs().forEach(job -> job.getSkills().remove(currentSkill));
        currentSkill.getSubscribers().forEach(subscriber -> subscriber.getSkills().remove(currentSkill));
        this.skillRepository.deleteById(id);
    }
    @Override
    public Skill handleGetSkillById(Long id){
        return this.skillRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Skill not found"));
    }
    @Override
    public ResultPaginationDTO handleGetAllSkill(Specification<Skill> specification, Pageable pageable){
        Page<Skill> skillPage = this.skillRepository.findAll(specification, pageable);
        ResultPaginationDTO rs = new ResultPaginationDTO();
        Meta meta = new Meta();
        meta.setPage(pageable.getPageNumber() + 1);
        meta.setPageSize(pageable.getPageSize());
        meta.setPages(skillPage.getTotalPages());
        meta.setTotal(skillPage.getTotalElements());
        rs.setMeta(meta);
        rs.setResult(skillPage.getContent());
        return rs;
    }
}

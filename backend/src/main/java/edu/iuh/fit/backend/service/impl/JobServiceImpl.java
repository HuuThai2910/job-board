/*
 * @ (#) .java    1.0
 * Copyright (c)  IUH. All rights reserved.
 */
package edu.iuh.fit.backend.service.impl;

import edu.iuh.fit.backend.domain.Job;
import edu.iuh.fit.backend.domain.Skill;
import edu.iuh.fit.backend.dto.Meta;
import edu.iuh.fit.backend.dto.ResultPaginationDTO;
import edu.iuh.fit.backend.dto.response.CreateJobResponse;
import edu.iuh.fit.backend.dto.response.JobResponse;
import edu.iuh.fit.backend.dto.response.UpdateJobResponse;
import edu.iuh.fit.backend.mapper.JobMapper;
import edu.iuh.fit.backend.repository.JobRepository;
import edu.iuh.fit.backend.repository.SkillRepository;
import edu.iuh.fit.backend.service.JobService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

/*
 * @description
 * @author: Huu Thai
 * @date:
 * @version: 1.0
 */
@Service
@AllArgsConstructor
public class JobServiceImpl implements JobService {
    private final JobRepository jobRepository;
    private final SkillRepository skillRepository;
    private final JobMapper jobMapper;

    @Override
    public CreateJobResponse handleCreateJob(Job job){
        List<Skill> skills = this.skillRepository.findByIdIn(
                job.getSkills().stream().map(Skill::getId).toList());
        job.setSkills(skills);
        Job newJob = this.jobRepository.save(job);
        return this.jobMapper.toCreateJobResponse(newJob);
    }
    @Override
    public UpdateJobResponse handleUpdateJob(Job job){
        List<Skill> skills = this.skillRepository.findByIdIn(
                job.getSkills().stream().map(Skill::getId).toList()
        );
        return this.jobRepository.findById(job.getId())
                .map(job1 -> {
                    job1.setName(job.getName());
                    job1.setLocation(job.getLocation());
                    job1.setSalary(job.getSalary());
                    job1.setQuantity(job.getQuantity());
                    job1.setLevel(job.getLevel());
                    job1.setDescription(job.getDescription());
                    job1.setActive(job.isActive());
                    job1.setSkills(job.getSkills() != null ? skills : job1.getSkills());
                    job1.setCompany(job.getCompany() != null ? job.getCompany() : job1.getCompany());
                    Job updateJob = this.jobRepository.save(job1);
                    return this.jobMapper.toUpdateJobResponse(updateJob);
                }).orElseThrow(() -> new NoSuchElementException("Job not found"));
    }
    @Override
    public void handleDeleteJob(Long id){
        if(this.jobRepository.findById(id).isEmpty()){
            throw new NoSuchElementException("Job not found");
        }
        this.jobRepository.deleteById(id);
    }
    @Override
    public JobResponse handleGetJobById(Long id){
        Job currentJob =  this.jobRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Job not found"));
        return this.jobMapper.toJobResponse(currentJob);
    }
    @Override
    public ResultPaginationDTO handleGetAllJob(Specification<Job> specification, Pageable pageable){
        Page<Job> jobPage = this.jobRepository.findAll(specification, pageable);
        ResultPaginationDTO rs = new ResultPaginationDTO();
        Meta meta = new Meta();
        meta.setPage(pageable.getPageNumber() + 1);
        meta.setPageSize(pageable.getPageSize());
        meta.setPages(jobPage.getTotalPages());
        meta.setTotal(jobPage.getTotalElements());
        rs.setMeta(meta);
        rs.setResult(this.jobMapper.toListJobResponse(jobPage.getContent()));
        return rs;
    }
}


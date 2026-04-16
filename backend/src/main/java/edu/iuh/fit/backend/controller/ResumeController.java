/*
 * @ (#) .java    1.0
 * Copyright (c)  IUH. All rights reserved.
 */
package edu.iuh.fit.backend.controller;

import com.turkraft.springfilter.boot.Filter;
import com.turkraft.springfilter.builder.FilterBuilder;
import com.turkraft.springfilter.converter.FilterSpecificationConverter;
import edu.iuh.fit.backend.domain.Company;
import edu.iuh.fit.backend.domain.Job;
import edu.iuh.fit.backend.domain.Resume;
import edu.iuh.fit.backend.domain.User;
import edu.iuh.fit.backend.dto.ResultPaginationDTO;
import edu.iuh.fit.backend.dto.response.CreateResumeResponse;
import edu.iuh.fit.backend.dto.response.ResumeResponse;
import edu.iuh.fit.backend.dto.response.UpdateResumeResponse;
import edu.iuh.fit.backend.service.ResumeService;
import edu.iuh.fit.backend.service.UserService;
import edu.iuh.fit.backend.util.SecurityUtil;
import edu.iuh.fit.backend.util.annotaion.ApiMessage;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/*
 * @description
 * @author: Huu Thai
 * @date:
 * @version: 1.0
 */
@RestController
@RequestMapping("/api/v1")
@AllArgsConstructor
public class ResumeController {
    private final ResumeService resumeService;
    private final UserService userService;
    private final FilterSpecificationConverter filterSpecificationConverter;
    private final FilterBuilder filterBuilder;

    @PostMapping("/resumes")
    @ApiMessage("Create resume successfully")
    public ResponseEntity<CreateResumeResponse> createResume(@RequestBody Resume resume){
        CreateResumeResponse createResumeResponse = this.resumeService.handleCreateResume(resume);
        return ResponseEntity.status(HttpStatus.CREATED).body(createResumeResponse);
    }

    @GetMapping("/resumes")
    @ApiMessage("Fetch all resume with paginate")
    public ResponseEntity<ResultPaginationDTO> getAllCompanies(@Filter Specification<Resume> specification, Pageable pageable){
        List<Long> jobIds = null;
        String email = SecurityUtil.getCurrentUserLogin().isPresent()
                ? SecurityUtil.getCurrentUserLogin().get()
                : "";
        User currentUser = this.userService.getUserByUserName(email);
        if(currentUser != null){
            Company userCompany = currentUser.getCompany();
            if(userCompany != null){
                List<Job> companyJob = userCompany.getJobs();
                if(companyJob != null && !companyJob.isEmpty()){
                    jobIds = companyJob.stream().map(Job::getId).toList();
                }
            }
        }
        Specification<Resume> jobInSpec = filterSpecificationConverter.convert(
                filterBuilder.field("job").in(filterBuilder.input(jobIds)).get());
        Specification<Resume> finalSpec = jobInSpec.and(specification);
        return ResponseEntity.ok(this.resumeService.handleGetAllResume(finalSpec, pageable));
    }

    @GetMapping("/resumes/{id}")
    @ApiMessage("Fetch resume")
    public ResponseEntity<ResumeResponse> getCompanyById(@PathVariable Long id){
        ResumeResponse company = this.resumeService.handleGetResumeById(id);
        return ResponseEntity.ok(company);
    }

    @PutMapping("/resumes")
    @ApiMessage("Update resume successfully")
    public ResponseEntity<UpdateResumeResponse> updateCompany(@RequestBody Resume resume){
        UpdateResumeResponse updateResume = this.resumeService.handleUpdateResume(resume);
        return ResponseEntity.ok(updateResume);
    }

    @DeleteMapping("/resumes/{id}")
    @ApiMessage("Delete resume successfully")
    public ResponseEntity<Void> deleteCompany(@PathVariable Long id){
        this.resumeService.handleDeleteResume(id);
        return ResponseEntity.ok(null);
    }

    @PostMapping("/resumes/by-user")
    @ApiMessage("Get list resumes by user")
    public ResponseEntity<ResultPaginationDTO> getResumesByUser(Pageable pageable){
        return ResponseEntity.ok().body(this.resumeService.getResumeByUser(pageable));
    }
}

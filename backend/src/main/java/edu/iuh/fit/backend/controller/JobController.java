/*
 * @ (#) .java    1.0
 * Copyright (c)  IUH. All rights reserved.
 */
package edu.iuh.fit.backend.controller;

import com.turkraft.springfilter.boot.Filter;
import edu.iuh.fit.backend.domain.Job;
import edu.iuh.fit.backend.dto.ResultPaginationDTO;
import edu.iuh.fit.backend.dto.response.CreateJobResponse;
import edu.iuh.fit.backend.dto.response.JobResponse;
import edu.iuh.fit.backend.dto.response.UpdateJobResponse;
import edu.iuh.fit.backend.service.JobService;
import edu.iuh.fit.backend.util.annotaion.ApiMessage;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
public class JobController {
    private final JobService jobService;

    @PostMapping("/jobs")
    @ApiMessage("Create job successfully")
    public ResponseEntity<CreateJobResponse> createJob(@RequestBody Job job){
        CreateJobResponse newJob = this.jobService.handleCreateJob(job);
        return ResponseEntity.status(HttpStatus.CREATED).body(newJob);
    }

    @PutMapping("/jobs")
    @ApiMessage("Update job successfully")
    public ResponseEntity<UpdateJobResponse> updateJob(@RequestBody Job job){
        UpdateJobResponse updatedJob = this.jobService.handleUpdateJob(job);
        return ResponseEntity.ok(updatedJob);
    }

    @DeleteMapping("/jobs/{id}")
    @ApiMessage("Delete job successfully")
    public ResponseEntity<Void> deleteJob(@PathVariable Long id){
        this.jobService.handleDeleteJob(id);
        return ResponseEntity.ok(null);
    }

    @GetMapping("/jobs")
    @ApiMessage("Fetch all job successfully")
    public ResponseEntity<ResultPaginationDTO> getAllJobs(@Filter Specification<Job> specification, Pageable pageable){
        return ResponseEntity.ok(this.jobService.handleGetAllJob(specification, pageable));
    }

    @GetMapping("/jobs/{id}")
    @ApiMessage("Fetch job successfully")
    public ResponseEntity<JobResponse> getJobById(@PathVariable Long id){
        return ResponseEntity.ok(this.jobService.handleGetJobById(id));
    }
}

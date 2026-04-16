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

import edu.iuh.fit.backend.domain.Job;
import edu.iuh.fit.backend.dto.ResultPaginationDTO;
import edu.iuh.fit.backend.dto.response.CreateJobResponse;
import edu.iuh.fit.backend.dto.response.JobResponse;
import edu.iuh.fit.backend.dto.response.UpdateJobResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

public interface JobService {
    CreateJobResponse handleCreateJob(Job job);

    UpdateJobResponse handleUpdateJob(Job job);

    void handleDeleteJob(Long id);

    JobResponse handleGetJobById(Long id);

    ResultPaginationDTO handleGetAllJob(Specification<Job> specification, Pageable pageable);
}

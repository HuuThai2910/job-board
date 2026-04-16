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

import edu.iuh.fit.backend.domain.Resume;
import edu.iuh.fit.backend.dto.ResultPaginationDTO;
import edu.iuh.fit.backend.dto.response.CreateResumeResponse;
import edu.iuh.fit.backend.dto.response.ResumeResponse;
import edu.iuh.fit.backend.dto.response.UpdateResumeResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

public interface ResumeService {
    CreateResumeResponse handleCreateResume(Resume resume);

    UpdateResumeResponse handleUpdateResume(Resume resume);

    void handleDeleteResume(Long id);

    ResumeResponse handleGetResumeById(Long id);

    ResultPaginationDTO handleGetAllResume(Specification<Resume> specification, Pageable pageable);

    ResultPaginationDTO getResumeByUser(Pageable pageable);
}

/*
 * @ (#) .java    1.0
 * Copyright (c)  IUH. All rights reserved.
 */
package edu.iuh.fit.backend.mapper;

import edu.iuh.fit.backend.domain.Job;
import edu.iuh.fit.backend.domain.Resume;
import edu.iuh.fit.backend.domain.Skill;
import edu.iuh.fit.backend.domain.User;
import edu.iuh.fit.backend.dto.response.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

/*
 * @description
 * @author: Huu Thai
 * @date:
 * @version: 1.0
 */
@Mapper(componentModel = "spring")
public interface ResumeMapper {
    CreateResumeResponse toCreateResumeResponse(Resume resume);

    UpdateResumeResponse toUpdateResumeResponse(Resume resume);

    @Mapping(target = "companyName", source = "job.company.name")
    ResumeResponse toResumeResponse(Resume resume);

    List<ResumeResponse> toListResumeResponse(List<Resume> resumes);

    JobSummaryResponse toJobSummaryResponse(Job job);

    UserSummaryResponse toUserSummaryResponse(User user);

}

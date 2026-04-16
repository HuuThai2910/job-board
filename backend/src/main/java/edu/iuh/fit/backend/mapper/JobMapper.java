/*
 * @ (#) .java    1.0
 * Copyright (c)  IUH. All rights reserved.
 */
package edu.iuh.fit.backend.mapper;

import edu.iuh.fit.backend.domain.Company;
import edu.iuh.fit.backend.domain.Job;
import edu.iuh.fit.backend.domain.Skill;
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
public interface JobMapper {
    JobResponse toJobResponse(Job job);

    @Mapping(target = "skills", qualifiedByName = "mapSkillsToNames")
    CreateJobResponse toCreateJobResponse(Job job);

    @Mapping(target = "skills", qualifiedByName = "mapSkillsToNames")
    UpdateJobResponse toUpdateJobResponse(Job job);

    List<JobResponse> toListJobResponse(List<Job> jobs);

    CompanySummaryResponse toCompanySummaryResponse(Company company);


    // --- Custom mapper method ---
    @Named("mapSkillsToNames")
    default List<String> mapSkillsToNames(List<Skill> skills) {
        if (skills == null) return List.of();
        return skills.stream().map(Skill::getName).toList();
    }


}

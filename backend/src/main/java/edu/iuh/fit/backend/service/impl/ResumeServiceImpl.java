/*
 * @ (#) .java    1.0
 * Copyright (c)  IUH. All rights reserved.
 */
package edu.iuh.fit.backend.service.impl;

import com.turkraft.springfilter.builder.FilterBuilder;
import com.turkraft.springfilter.converter.FilterSpecification;
import com.turkraft.springfilter.converter.FilterSpecificationConverter;
import com.turkraft.springfilter.parser.FilterParser;
import com.turkraft.springfilter.parser.node.FilterNode;
import edu.iuh.fit.backend.domain.Resume;
import edu.iuh.fit.backend.dto.Meta;
import edu.iuh.fit.backend.dto.ResultPaginationDTO;
import edu.iuh.fit.backend.dto.response.CreateResumeResponse;
import edu.iuh.fit.backend.dto.response.ResumeResponse;
import edu.iuh.fit.backend.dto.response.UpdateResumeResponse;
import edu.iuh.fit.backend.mapper.ResumeMapper;
import edu.iuh.fit.backend.repository.JobRepository;
import edu.iuh.fit.backend.repository.ResumeRepository;
import edu.iuh.fit.backend.repository.UserRepository;
import edu.iuh.fit.backend.util.SecurityUtil;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

/*
 * @description
 * @author: Huu Thai
 * @date:
 * @version: 1.0
 */
@Service
@AllArgsConstructor
public class ResumeServiceImpl implements edu.iuh.fit.backend.service.ResumeService {
    private final ResumeRepository resumeRepository;
    private final UserRepository userRepository;
    private final JobRepository jobRepository;
    private final ResumeMapper resumeMapper;
    private FilterBuilder filterBuilder;
    private final FilterParser filterParser;
    private final FilterSpecificationConverter filterSpecificationConverter;

    @Override
    public CreateResumeResponse handleCreateResume(Resume resume){
        if(this.userRepository.findById(resume.getUser().getId()).isEmpty())
            throw new NoSuchElementException("User not found");
        if(this.jobRepository.findById(resume.getJob().getId()).isEmpty())
            throw new NoSuchElementException("Job not found");
        Resume newResume = this.resumeRepository.save(resume);
        return this.resumeMapper.toCreateResumeResponse(resume);
    }

    @Override
    public UpdateResumeResponse handleUpdateResume(Resume resume){
        Resume currentResume = this.resumeRepository.findById(resume.getId())
                .orElseThrow(() -> new NoSuchElementException("Resume not found"));
        if(this.userRepository.findById(currentResume.getUser().getId()).isEmpty())
            throw new NoSuchElementException("User not found");
        if(this.jobRepository.findById(currentResume.getJob().getId()).isEmpty())
            throw new NoSuchElementException("Job not found");
        currentResume.setStatus(resume.getStatus());
        return this.resumeMapper.toUpdateResumeResponse(this.resumeRepository.save(currentResume));

    }

    @Override
    public void handleDeleteResume(Long id){
        if(this.resumeRepository.findById(id).isEmpty())
            throw new NoSuchElementException("Resume not found");
        this.resumeRepository.deleteById(id);
    }

    @Override
    public ResumeResponse handleGetResumeById(Long id){
        return this.resumeRepository.findById(id).map(this.resumeMapper::toResumeResponse)
                .orElseThrow(() -> new NoSuchElementException("Resume not found"));
    }

    @Override
    public ResultPaginationDTO handleGetAllResume(Specification<Resume> specification, Pageable pageable){
        Page<Resume> resumePage = this.resumeRepository.findAll(specification, pageable);
        ResultPaginationDTO resultPaginationDTO = new ResultPaginationDTO();
        Meta meta = new Meta();
        meta.setPages(pageable.getPageSize());
        meta.setPage(pageable.getPageNumber() + 1);
        meta.setPageSize(resumePage.getTotalPages());
        meta.setTotal(resumePage.getTotalElements());
        resultPaginationDTO.setMeta(meta);
        resultPaginationDTO.setResult(this.resumeMapper.toListResumeResponse(resumePage.getContent()));
        return resultPaginationDTO;
    }

    @Override
    public ResultPaginationDTO getResumeByUser(Pageable pageable) {
        // query builder
        String email = SecurityUtil.getCurrentUserLogin().isPresent()
                ? SecurityUtil.getCurrentUserLogin().get()
                : "";
        FilterNode node = filterParser.parse("email='" + email + "'");
        FilterSpecification<Resume> spec = filterSpecificationConverter.convert(node);
        Page<Resume> pageResume = this.resumeRepository.findAll(spec, pageable);
        ResultPaginationDTO rs = new ResultPaginationDTO();
        Meta meta = new Meta();
        meta.setPage(pageable.getPageNumber() + 1);
        meta.setPageSize(pageable.getPageSize());
        meta.setPages(pageResume.getTotalPages());
        meta.setTotal(pageResume.getTotalElements());
        rs.setMeta(meta);
        rs.setResult(this.resumeMapper.toListResumeResponse(pageResume.getContent()));
        return rs;
    }
}

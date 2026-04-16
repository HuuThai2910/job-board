/*
 * @ (#) .java    1.0
 * Copyright (c)  IUH. All rights reserved.
 */
package edu.iuh.fit.backend.service.impl;

import edu.iuh.fit.backend.domain.Company;
import edu.iuh.fit.backend.domain.User;
import edu.iuh.fit.backend.dto.Meta;
import edu.iuh.fit.backend.dto.ResultPaginationDTO;
import edu.iuh.fit.backend.repository.CompanyRepository;
import edu.iuh.fit.backend.repository.UserRepository;
import edu.iuh.fit.backend.service.CompanyService;
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
public class CompanyServiceImpl implements CompanyService {
    private final CompanyRepository companyRepository;
    private final UserRepository userRepository;

    @Override
    public Company handleCreateCompany(Company company){
        return this.companyRepository.save(company);
    }

    @Override
    public ResultPaginationDTO handleGetAllCompanies(Specification<Company> specification, Pageable pageable){
        Page<Company> companyPage = this.companyRepository.findAll(specification, pageable);
        ResultPaginationDTO rs = new ResultPaginationDTO();
        Meta meta = new Meta();
        meta.setPage(pageable.getPageNumber() + 1);
        meta.setPageSize(pageable.getPageSize());
        meta.setPages(companyPage.getTotalPages());
        meta.setTotal(companyPage.getTotalElements());
        rs.setMeta(meta);
        rs.setResult(companyPage.getContent());
        return rs;
    }

    @Override
    public Company handleGetCompanyById(Long id){
        return this.companyRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Company not found"));
    }

    @Override
    public Company handleUpdateCompany(Company updatedCompany){
        return this.companyRepository.findById(updatedCompany.getId())
                .map(company -> {
                    company.setName(updatedCompany.getName());
                    company.setDescription(updatedCompany.getDescription());
                    company.setAddress(updatedCompany.getAddress());
                    company.setLogo(updatedCompany.getLogo());
                    return this.companyRepository.save(company);
                }).orElseThrow(() -> new NoSuchElementException("Company not found"));
    }

    @Override
    public void handleDeleteCompany(Long id){
        Company company = companyRepository.findById(id).orElse(null);
        if(company == null){
            throw new NoSuchElementException("Company not found");
        }
        List<User> users = company.getUsers();
        this.userRepository.deleteAll(users);
        this.companyRepository.deleteById(id);
    }

}

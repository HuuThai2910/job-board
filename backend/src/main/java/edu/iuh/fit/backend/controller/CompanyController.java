/*
 * @ (#) .java    1.0
 * Copyright (c)  IUH. All rights reserved.
 */
package edu.iuh.fit.backend.controller;

import com.turkraft.springfilter.boot.Filter;
import edu.iuh.fit.backend.domain.Company;
import edu.iuh.fit.backend.dto.ResultPaginationDTO;
import edu.iuh.fit.backend.service.CompanyService;
import edu.iuh.fit.backend.service.impl.CompanyServiceImpl;
import edu.iuh.fit.backend.util.annotaion.ApiMessage;
import jakarta.validation.Valid;
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
@RequestMapping("/api/v1")
public class CompanyController {

    private final CompanyService companyService;

    public CompanyController(CompanyServiceImpl companyService) {
        this.companyService = companyService;
    }

    @PostMapping("/companies")
    @ApiMessage("Create company successfully")
    public ResponseEntity<Company> createCompany(@RequestBody @Valid Company company){
        Company newCompany = this.companyService.handleCreateCompany(company);
        return ResponseEntity.status(HttpStatus.CREATED).body(newCompany);
    }

    @GetMapping("/companies")
    @ApiMessage("Fetch all companies")
    public ResponseEntity<ResultPaginationDTO> getAllCompanies(@Filter Specification<Company> specification,Pageable pageable){
        return ResponseEntity.ok(this.companyService.handleGetAllCompanies(specification, pageable));
    }

    @GetMapping("/companies/{id}")
    @ApiMessage("Fetch company")
    public ResponseEntity<Company> getCompanyById(@PathVariable Long id){
        Company company = this.companyService.handleGetCompanyById(id);
        return ResponseEntity.ok(company);
    }

    @PutMapping("/companies")
    @ApiMessage("Update company successfully")
    public ResponseEntity<Company> updateCompany(@RequestBody @Valid Company updatedCompany){
        Company company = this.companyService.handleUpdateCompany(updatedCompany);
        return ResponseEntity.ok(company);
    }

    @DeleteMapping("/companies/{id}")
    @ApiMessage("Delete company successfully")
    public ResponseEntity<Void> deleteCompany(@PathVariable Long id){
        this.companyService.handleDeleteCompany(id);
        return ResponseEntity.ok(null);
    }
}

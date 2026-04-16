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

import edu.iuh.fit.backend.domain.Company;
import edu.iuh.fit.backend.dto.ResultPaginationDTO;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;


public interface CompanyService {
    Company handleCreateCompany(Company company);

    ResultPaginationDTO handleGetAllCompanies(Specification<Company> specification, Pageable pageable);

    Company handleGetCompanyById(Long id);

    Company handleUpdateCompany(Company updatedCompany);

    void handleDeleteCompany(Long id);
}

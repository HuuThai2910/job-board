/*
 * @ (#) .java    1.0
 * Copyright (c)  IUH. All rights reserved.
 */
package edu.iuh.fit.backend.service.impl;

import edu.iuh.fit.backend.domain.Permission;
import edu.iuh.fit.backend.domain.Resume;
import edu.iuh.fit.backend.dto.Meta;
import edu.iuh.fit.backend.dto.ResultPaginationDTO;
import edu.iuh.fit.backend.repository.PermissionRepository;
import edu.iuh.fit.backend.service.PermissionService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Optional;

/*
 * @description
 * @author: Huu Thai
 * @date:
 * @version: 1.0
 */
@Service
@AllArgsConstructor
public class PermissionServiceImpl implements PermissionService {
    private final PermissionRepository permissionRepository;

    public boolean isPermissionExist (Permission p) {
        return permissionRepository.existsByModuleAndApiPathAndMethod(
                p.getModule(),
                p.getApiPath(),
                p.getMethod());
    }

    public Permission create(Permission p) {
        return permissionRepository.save(p);
    }

    public Permission fetchById(long id) {
        Optional<Permission> permissionOptional = this.permissionRepository.findById(id);
        if (permissionOptional.isPresent())
            return permissionOptional.get();
        return null;
    }

    public Permission update (Permission p) {
        Permission permissionDB = this.fetchById(p.getId());
        if (permissionDB != null) {
            permissionDB.setName(p.getName());
            permissionDB.setApiPath(p.getApiPath());
            permissionDB.setMethod(p.getMethod());
            permissionDB.setModule(p.getModule());
        // update
            permissionDB = this.permissionRepository.save(permissionDB);
            return permissionDB;
        }
        return null;
    }

    public void delete(long id) {
        // delete permission_role
        Optional<Permission> permissionOptional = this.permissionRepository.findById(id);
        Permission currentPermission = permissionOptional.get();
        currentPermission.getRoles().forEach(role -> role.getPermissions().remove(currentPermission));
        // delete permission
        this.permissionRepository.delete(currentPermission);
    }

    public ResultPaginationDTO getPermissions(Specification<Permission> spec, Pageable pageable) {
        Page<Permission> permissionPage = this.permissionRepository.findAll(spec, pageable);
        ResultPaginationDTO resultPaginationDTO = new ResultPaginationDTO();
        Meta meta = new Meta();
        meta.setPageSize(pageable.getPageSize());
        meta.setPage(pageable.getPageNumber() + 1);
        meta.setPages(permissionPage.getTotalPages());
        meta.setTotal(permissionPage.getTotalElements());
        resultPaginationDTO.setMeta(meta);
        resultPaginationDTO.setResult(permissionPage.getContent());
        return resultPaginationDTO;
    }

    public boolean isSameName(Permission p) {
        Permission permissionDB = this.fetchById(p.getId());
        if(permissionDB != null){
            return permissionDB.getName().equals(p.getName());
        }
        return false;
    }
}

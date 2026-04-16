/*
 * @ (#) .java    1.0
 * Copyright (c)  IUH. All rights reserved.
 */
package edu.iuh.fit.backend.service.impl;

import edu.iuh.fit.backend.domain.Permission;
import edu.iuh.fit.backend.domain.Role;
import edu.iuh.fit.backend.dto.Meta;
import edu.iuh.fit.backend.dto.ResultPaginationDTO;
import edu.iuh.fit.backend.repository.PermissionRepository;
import edu.iuh.fit.backend.repository.RoleRepository;
import edu.iuh.fit.backend.service.RoleService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/*
 * @description
 * @author: Huu Thai
 * @date:
 * @version: 1.0
 */
@Service
@AllArgsConstructor
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;

    @Override
    public boolean existByName(String name) {
        return this.roleRepository.existsByName(name);
    }

    @Override
    public Role create (Role r) {
        // check permissions
        if (r.getPermissions() != null) {
            List<Long> reqPermissions = r.getPermissions()
                    .stream().map(x -> x.getId())
                    .collect(Collectors.toList());
            List<Permission> dbPermissions = this.permissionRepository.findByIdIn(reqPermissions);
            r.setPermissions(dbPermissions);
        }
        return this.roleRepository.save(r);
    }

    @Override
    public Role fetchById(Long id) {
        Optional<Role> roleOptional = this.roleRepository.findById(id);
        return roleOptional.orElse(null);
    }

    @Override
    public Role update(Role r) {
        Role roleDB = this.fetchById(r.getId());
        if (r.getPermissions() != null) {
            List<Long> reqPermissions = r.getPermissions()
                    .stream().map(Permission::getId)
                    .collect(Collectors.toList());
            List<Permission> dbPermissions = this.permissionRepository.findByIdIn(reqPermissions);
            r.setPermissions(dbPermissions);
        }
        roleDB.setName (r.getName());
        roleDB.setDescription (r.getDescription());
        roleDB.setActive (r.isActive());
        roleDB.setPermissions(r.getPermissions());
        roleDB = this.roleRepository.save(roleDB);
        return roleDB;
    }

    @Override
    public void delete(long id){
        this.roleRepository.deleteById(id);
    }

    @Override
    public ResultPaginationDTO getRoles(Specification<Role> spec, Pageable pageable) {
        Page<Role> permissionPage = this.roleRepository.findAll(spec, pageable);
        ResultPaginationDTO resultPaginationDTO = new ResultPaginationDTO();
        Meta meta = new Meta();
        meta.setPage(pageable.getPageNumber() + 1);
        meta.setPageSize(pageable.getPageSize());
        meta.setPages(permissionPage.getTotalPages());
        meta.setTotal(permissionPage.getTotalElements());
        resultPaginationDTO.setMeta(meta);
        resultPaginationDTO.setResult(permissionPage.getContent());
        return resultPaginationDTO;
    }
}

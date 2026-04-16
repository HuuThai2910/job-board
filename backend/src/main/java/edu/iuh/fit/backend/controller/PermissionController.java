/*
 * @ (#) .java    1.0
 * Copyright (c)  IUH. All rights reserved.
 */
package edu.iuh.fit.backend.controller;

import com.turkraft.springfilter.boot.Filter;
import edu.iuh.fit.backend.domain.Permission;
import edu.iuh.fit.backend.domain.Skill;
import edu.iuh.fit.backend.dto.ResultPaginationDTO;
import edu.iuh.fit.backend.service.PermissionService;
import edu.iuh.fit.backend.service.impl.PermissionServiceImpl;
import edu.iuh.fit.backend.util.annotaion.ApiMessage;
import edu.iuh.fit.backend.util.error.InvalidException;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
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
@AllArgsConstructor
public class PermissionController {
    private final PermissionServiceImpl permissionService;
    @PostMapping("/permissions")
    @ApiMessage("Create a permission")
    public ResponseEntity<Permission> create(@RequestBody Permission p) throws InvalidException {
        // check exist
        if (this.permissionService.isPermissionExist(p)) {
            throw new InvalidException("Permission đã tồn tại.");
        }
        // create new permission
        return ResponseEntity.status (HttpStatus.CREATED).body(this.permissionService.create(p));
    }

    @PutMapping("/permissions")
    @ApiMessage("Update a permission")
    public ResponseEntity<Permission> update (@RequestBody Permission p) throws InvalidException {
        // check exist by id
        if (this.permissionService.fetchById(p.getId()) == null) {
            throw new InvalidException("Permission với id = " + p.getId() + "không tồn tại");
        }
        // check exist by module, apiPath and method
        if (this.permissionService.isPermissionExist(p)) {
//            Check name
            if(this.permissionService.isSameName(p)){
                throw new InvalidException("Permission đã tồn tại.");
            }

        }
        return ResponseEntity.ok(this.permissionService.update(p));
    }

    @DeleteMapping("/permissions/{id}")
    @ApiMessage("delete a permission")
    public ResponseEntity<Void> delete (@PathVariable("id") long id) throws InvalidException {
        // check exist by id
        if (this.permissionService.fetchById(id) == null) {
            throw new InvalidException("Permission với id = " + id + " không tồn tại.");
        }
        this.permissionService.delete(id);
        return ResponseEntity.ok().body(null);
    }
    @GetMapping("/permissions")
    @ApiMessage("Fetch permissions")
    public ResponseEntity<ResultPaginationDTO> getPermissions(
            @Filter Specification<Permission> spec, Pageable pageable) {
        return ResponseEntity.ok(this.permissionService.getPermissions(spec, pageable));
    }
    @GetMapping("/permissions/{id}")
    @ApiMessage("Fetch permission successfully")
    public ResponseEntity<Permission> getPermissionById(@PathVariable Long id){
        return ResponseEntity.ok(this.permissionService.fetchById(id));
    }
}

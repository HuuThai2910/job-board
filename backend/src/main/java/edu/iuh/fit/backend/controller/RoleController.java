/*
 * @ (#) .java    1.0
 * Copyright (c)  IUH. All rights reserved.
 */
package edu.iuh.fit.backend.controller;

import com.turkraft.springfilter.boot.Filter;
import edu.iuh.fit.backend.domain.Permission;
import edu.iuh.fit.backend.domain.Role;
import edu.iuh.fit.backend.dto.ResultPaginationDTO;
import edu.iuh.fit.backend.service.RoleService;
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
public class RoleController {
    private final RoleService roleService;

    @PostMapping("/roles")
    @ApiMessage("Create a role")
    public ResponseEntity<Role> create(@RequestBody Role r) throws InvalidException {
        // check name
        if (this.roleService.existByName(r.getName())) {
            throw new InvalidException("Role với name = " + r.getName() + " đã tồn tại");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(this.roleService.create(r));
    }

    @PutMapping("/roles")
    @ApiMessage("Update a role")
    public ResponseEntity<Role> update (@RequestBody Role r) throws InvalidException{
        // check-id
        if (this.roleService.fetchById(r.getId()) == null) {
                 throw new InvalidException("Role với id = " + r.getId() + "không tồn tại");
        }
//        // check name
//                if (this.roleService.existByName(r.getName())) {
//                    throw new InvalidException("Role với name = " + r.getName() + "đã tồn tại");
//                }
                return ResponseEntity.ok(this.roleService.update(r));
    }

    @DeleteMapping("/roles/{id}")
    @ApiMessage("delete a role")
    public ResponseEntity<Void> delete (@PathVariable("id") long id) throws InvalidException {
        // check exist by id
        if (this.roleService.fetchById(id) == null) {
            throw new InvalidException("Role với id = " + id + " không tồn tại.");
        }
        this.roleService.delete(id);
        return ResponseEntity.ok().body(null);
    }

    @GetMapping("/roles")
    @ApiMessage("Fetch roles")
    public ResponseEntity<ResultPaginationDTO> getRoles(
            @Filter Specification<Role> spec, Pageable pageable) {
        return ResponseEntity.ok(this.roleService.getRoles(spec, pageable));
    }
    @GetMapping("/roles/{id}")
    @ApiMessage("Fetch role successfully")
    public ResponseEntity<Role> getRoleById(@PathVariable Long id){
        return ResponseEntity.ok(this.roleService.fetchById(id));
    }
}

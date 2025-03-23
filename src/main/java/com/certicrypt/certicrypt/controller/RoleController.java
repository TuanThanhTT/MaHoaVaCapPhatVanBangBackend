package com.certicrypt.certicrypt.controller;


import com.certicrypt.certicrypt.models.Role;
import com.certicrypt.certicrypt.repository.RoleRepository;
import com.certicrypt.certicrypt.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/role")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @GetMapping(path="/all")
    public ResponseEntity<List<Role>> getAllRoles() {

        return ResponseEntity.ok(roleService.getAllRoles());

    }

    @PostMapping(path="/add")
    public ResponseEntity<Role> addRole(@RequestBody Role role ) {
        return ResponseEntity.ok(roleService.addRole(role));
    }

}

package com.certicrypt.certicrypt.controller;


import com.certicrypt.certicrypt.models.Role;

import com.certicrypt.certicrypt.service.RoleService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/role")
@CrossOrigin(origins = "http://localhost:5173")
public class RoleController {

    private final RoleService roleService;
    public  RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @GetMapping(path="/all")
    public ResponseEntity<List<Role>> getAllRoles() {
        return ResponseEntity.ok(roleService.getAllRoles());
    }

    @PostMapping(path="/add")
    public ResponseEntity<Role> addRole(@RequestBody Role role ) {
        return ResponseEntity.ok(roleService.addRole(role));
    }

}

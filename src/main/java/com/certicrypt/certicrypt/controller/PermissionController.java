package com.certicrypt.certicrypt.controller;

import com.certicrypt.certicrypt.models.Permission;
import com.certicrypt.certicrypt.repository.PermissionRepository;
import com.certicrypt.certicrypt.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/permission")
public class PermissionController {
    @Autowired
    private PermissionService permissionService;

    @GetMapping(path="/all")
    public ResponseEntity<List<Permission>> getAllPermissions() {
        List<Permission> permissions = permissionService.getAllPermissions();
        if (permissions.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(permissions, HttpStatus.OK);
    }

    @PostMapping(path="/add")
    public ResponseEntity<Permission> addPermission(@RequestBody Permission permission) {
        return ResponseEntity.ok(permissionService.addPermission(permission));
    }


}

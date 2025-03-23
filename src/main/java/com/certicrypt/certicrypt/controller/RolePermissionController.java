package com.certicrypt.certicrypt.controller;

import com.certicrypt.certicrypt.models.RolePermission;
import com.certicrypt.certicrypt.service.RolePermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/rolepermission")
public class RolePermissionController{

    @Autowired
    private RolePermissionService rolePermissionService;

    @GetMapping(path="/all")
    public ResponseEntity<List<RolePermission>> getALl(){
        List<RolePermission> rolePermissions = rolePermissionService.getALl();
        return new ResponseEntity<>(rolePermissions, HttpStatus.OK);
    }

    @PostMapping(path="/add")
    public ResponseEntity<RolePermission> addRolePermission(@RequestBody RolePermission rolePermission){

        return ResponseEntity.ok(rolePermissionService.addRolePermission(rolePermission));
    }

}

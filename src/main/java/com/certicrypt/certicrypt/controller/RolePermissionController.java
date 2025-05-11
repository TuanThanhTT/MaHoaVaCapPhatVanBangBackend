package com.certicrypt.certicrypt.controller;

import com.certicrypt.certicrypt.DTO.response.RolePermissionResponse;
import com.certicrypt.certicrypt.models.RolePermission;
import com.certicrypt.certicrypt.service.RolePermissionService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/admin/rolepermission")
@CrossOrigin(origins = "http://localhost:5173")
public class RolePermissionController{

    private final RolePermissionService rolePermissionService;
    public RolePermissionController(RolePermissionService rolePermissionService) {
        this.rolePermissionService = rolePermissionService;
    }
    @GetMapping(path="/all")
    public ResponseEntity<List<RolePermissionResponse>> getALl(){
        List<RolePermission> rolePermissions = rolePermissionService.getALl();

        Map<Integer, List<Integer>> groupedMap = rolePermissions.stream()
                .collect(Collectors.groupingBy(
                        RolePermission::getRoleId,
                        Collectors.mapping(RolePermission::getPermissionId, Collectors.toList())
                ));

        List<RolePermissionResponse> result = groupedMap.entrySet().stream()
                .map(entry -> new RolePermissionResponse(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping(path="/add")
    public ResponseEntity<RolePermission> addRolePermission(@RequestBody RolePermission rolePermission){
        return ResponseEntity.ok(rolePermissionService.addRolePermission(rolePermission));
    }

    @DeleteMapping(path="/delete")
    public ResponseEntity<Boolean> deleteRolePermission(@RequestBody RolePermission rolePermission){
        Boolean result =  rolePermissionService.deleteRolePermission(rolePermission);
        return result ? ResponseEntity.ok(true) : ResponseEntity.ok(false);
    }

}

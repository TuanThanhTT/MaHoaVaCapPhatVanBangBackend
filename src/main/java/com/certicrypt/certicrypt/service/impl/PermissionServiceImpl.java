package com.certicrypt.certicrypt.service.impl;

import com.certicrypt.certicrypt.models.Permission;
import com.certicrypt.certicrypt.repository.PermissionRepository;
import com.certicrypt.certicrypt.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service

public class PermissionServiceImpl  implements PermissionService {

    @Autowired
    private PermissionRepository permissionRepository;

    @Override
    public List<Permission> getAllPermissions() {
        return permissionRepository.findAll();
    }

    @Override
    public Permission addPermission(Permission permission) {
        return permissionRepository.save(permission);
    }
}

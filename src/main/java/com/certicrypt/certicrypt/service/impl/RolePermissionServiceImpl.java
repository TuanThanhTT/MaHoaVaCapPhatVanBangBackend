package com.certicrypt.certicrypt.service.impl;

import com.certicrypt.certicrypt.models.RolePermission;
import com.certicrypt.certicrypt.repository.RolePermissionRepository;
import com.certicrypt.certicrypt.service.RolePermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RolePermissionServiceImpl  implements RolePermissionService {

    @Autowired
    private RolePermissionRepository rolePermissionRepository;


    @Override
    public List<RolePermission> getALl() {
        return rolePermissionRepository.findAll();
    }

    @Override
    public RolePermission addRolePermission(RolePermission rolePermission) {
        return rolePermissionRepository.save(rolePermission);
    }
}

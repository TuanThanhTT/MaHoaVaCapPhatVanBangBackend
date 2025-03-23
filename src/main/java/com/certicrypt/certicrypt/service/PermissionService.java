package com.certicrypt.certicrypt.service;

import com.certicrypt.certicrypt.models.Permission;

import java.util.List;

public interface PermissionService {

    List<Permission> getAllPermissions();
    Permission addPermission(Permission permission);

}

package com.certicrypt.certicrypt.service;

import com.certicrypt.certicrypt.models.RolePermission;

import java.util.List;

public interface RolePermissionService {

    List<RolePermission>  getALl();
    RolePermission addRolePermission(RolePermission rolePermission);

}

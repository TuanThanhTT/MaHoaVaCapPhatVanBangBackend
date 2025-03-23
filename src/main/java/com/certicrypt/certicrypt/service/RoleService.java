package com.certicrypt.certicrypt.service;

import com.certicrypt.certicrypt.models.Role;

import java.util.List;

public interface RoleService {

    List<Role> getAllRoles();
    Role addRole(Role role);
}

package com.certicrypt.certicrypt.service.impl;

import com.certicrypt.certicrypt.models.Role;
import com.certicrypt.certicrypt.repository.RoleRepository;
import com.certicrypt.certicrypt.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleServiceImpl  implements RoleService {

    @Autowired
    private RoleRepository roleRepository;


    @Override
    public List<Role> getAllRoles() {
        return (List<Role>) roleRepository.findAll();
    }

    @Override
    public Role addRole(Role role) {
        return  roleRepository.save(role);
    }
}

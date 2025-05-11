package com.certicrypt.certicrypt.service.impl;

import com.certicrypt.certicrypt.models.RolePermission;
import com.certicrypt.certicrypt.models.RolePermissionId;
import com.certicrypt.certicrypt.repository.RolePermissionRepository;
import com.certicrypt.certicrypt.service.RolePermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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

    @Override
    public Boolean deleteRolePermission(RolePermission rolePermission) {
        try{

            if (rolePermission == null ||
                    rolePermission.getPermissionId() == null ||
                    rolePermission.getRoleId() == null) {
                return false;
            }

            RolePermissionId rolePermissionId = new RolePermissionId(
                    rolePermission.getRoleId(),
                    rolePermission.getPermissionId()
            );

            return rolePermissionRepository.findById(rolePermissionId)
                    .map(rp -> {
                        rolePermissionRepository.delete(rp);
                        return true;
                    })
                    .orElse(false);

        }catch (Exception e){
            throw  new RuntimeException(e.getMessage());
        }
    }
}

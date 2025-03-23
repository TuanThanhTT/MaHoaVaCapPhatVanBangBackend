package com.certicrypt.certicrypt.repository;

import com.certicrypt.certicrypt.models.RolePermission;
import com.certicrypt.certicrypt.models.RolePermissionId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RolePermissionRepository  extends JpaRepository<RolePermission, RolePermissionId> {
}

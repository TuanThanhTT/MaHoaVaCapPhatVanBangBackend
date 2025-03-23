package com.certicrypt.certicrypt.repository;

import com.certicrypt.certicrypt.models.Permission;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PermissionRepository  extends JpaRepository<Permission, Long> {
}

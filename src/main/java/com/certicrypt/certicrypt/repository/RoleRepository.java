package com.certicrypt.certicrypt.repository;

import com.certicrypt.certicrypt.models.Role;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface RoleRepository  extends CrudRepository<Role, Integer> {
    Optional<Role> findByRoleName(String roleName);
}

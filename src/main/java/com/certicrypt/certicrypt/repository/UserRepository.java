package com.certicrypt.certicrypt.repository;

import com.certicrypt.certicrypt.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
    User findByUserName(String userName);
    User findByEmail(String email);
    Page<User> findByIsLockedFalse(Pageable pageable);
}
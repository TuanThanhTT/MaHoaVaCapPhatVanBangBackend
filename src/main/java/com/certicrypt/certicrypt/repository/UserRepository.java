package com.certicrypt.certicrypt.repository;

import com.certicrypt.certicrypt.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Integer> {
    User findByUserName(String userName);
    User findByEmail(String email);
    Page<User> findByIsLockedFalse(Pageable pageable);

    Page<User> findByRole_RoleIdAndIsLockedFalse (Integer roleId ,Pageable pageable);
    @Query(value = """
    SELECT * FROM "User" u 
    WHERE unaccent(lower(u.username)) LIKE unaccent(lower(CONCAT('%', :username, '%')))
      AND u.islocked = false
    """,
            countQuery = """
    SELECT COUNT(*) FROM "User" u 
    WHERE unaccent(lower(u.username)) LIKE unaccent(lower(CONCAT('%', :username, '%')))
      AND u.islocked = false
    """,
            nativeQuery = true)
    Page<User> searchActiveUsersByUsernameIgnoreCaseAndAccent(@Param("username") String username, Pageable pageable);

}
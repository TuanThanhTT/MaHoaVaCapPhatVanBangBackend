package com.certicrypt.certicrypt.service;
import java.util.Collections;

import com.certicrypt.certicrypt.models.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService{

    @Autowired
    private EntityManager entityManager;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            // Truy vấn User từ database
            var query = entityManager.createQuery(
                    "SELECT u FROM User u WHERE u.userName = :username", User.class);
            query.setParameter("username", username);
            User user = query.getSingleResult();

            // Kiểm tra user null
            if (user == null) {
                throw new UsernameNotFoundException("Không tìm thấy người dùng: " + username);
            }

            // Kiểm tra role null
            if (user.getRole() == null) {
                throw new UsernameNotFoundException("Người dùng không có vai trò: " + username);
            }

            // Ánh xạ roleId thành vai trò
            String role;
            switch (user.getRole().getRoleId()) {
                case 1:
                    role = "ROLE_ADMINISTRATOR";
                    break;
                case 2:
                    role = "ROLE_ADMIN";
                    break;
                case 3:
                    role = "ROLE_STUDENT";
                    break;
                default:
                    throw new UsernameNotFoundException("Vai trò không hợp lệ cho người dùng: " + username);
            }

            // Log để debug
            System.out.println("User: " + username + ", Role: " + role);

//            return new org.springframework.security.core.userdetails.User(
//                    user.getUserName(),
//                    user.getPassWord(),
//                    Collections.singletonList(new SimpleGrantedAuthority(role))
//            );
            return new org.springframework.security.core.userdetails.User(
                    user.getUserName(),
                    user.getPassWord(),
                    true,                // ✅ enabled
                    true,                                // ✅ accountNonExpired
                    true,                                // ✅ credentialsNonExpired
                    !user.getIsLocked(),                 // ✅ accountNonLocked
                    Collections.singletonList(new SimpleGrantedAuthority(role))
            );
        } catch (NoResultException e) {
            throw new UsernameNotFoundException("Không tìm thấy người dùng: " + username);
        } catch (Exception e) {
            throw new UsernameNotFoundException("Lỗi khi tải người dùng: " + username, e);
        }
    }

}

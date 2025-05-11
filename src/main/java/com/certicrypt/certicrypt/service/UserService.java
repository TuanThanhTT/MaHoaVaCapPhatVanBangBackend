package com.certicrypt.certicrypt.service;

import com.certicrypt.certicrypt.DTO.request.UserRequest;
import com.certicrypt.certicrypt.DTO.response.UserResponse;
import com.certicrypt.certicrypt.models.Student;
import com.certicrypt.certicrypt.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {

    Page<UserResponse> getAll(Pageable pageable);;
    Page<UserResponse> getUserByRole(Integer roleId, Pageable pageable);
    Page<UserResponse> getUserbyUsername(String username, Pageable pageable);
    User updateUser(UserRequest userRequest);
    User addUser(UserRequest userRequest);
    Boolean deleteUser(Integer userId);
    Boolean unclockUser(Integer userId);
    User addUserAdmin(UserRequest userRequest);
    User getUserByUsername(String username);
    Boolean resetPassword(String username);

    String changePassword(String userName,String password, String oldPassword);

}

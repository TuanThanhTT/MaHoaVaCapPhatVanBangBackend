package com.certicrypt.certicrypt.mapper;

import com.certicrypt.certicrypt.DTO.response.UserResponse;
import com.certicrypt.certicrypt.models.User;

public class UserMapper {

    public static UserResponse toDTO(User user) {
        UserResponse dto = new UserResponse();
        dto.setId(user.getUserId());
        dto.setUserName(user.getUserName());
        dto.setEmail(user.getEmail());
        dto.setRoleId(user.getRole().getRoleId());
        dto.setIsClocked(user.getIsLocked());
        return dto;
    }
}

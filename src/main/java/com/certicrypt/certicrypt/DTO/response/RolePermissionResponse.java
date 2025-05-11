package com.certicrypt.certicrypt.DTO.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class RolePermissionResponse {
    private int roleId;
    private List<Integer> permissionIds;
    public RolePermissionResponse(int roleId, List<Integer> permissionIds) {
        this.roleId = roleId;
        this.permissionIds = permissionIds;
    }
}


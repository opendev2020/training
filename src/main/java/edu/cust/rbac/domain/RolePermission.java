package edu.cust.rbac.domain;

import lombok.Data;

@Data
public class RolePermission {
	
    private String roleId;
    
    private String permId;
}

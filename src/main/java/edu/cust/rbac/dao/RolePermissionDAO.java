package edu.cust.rbac.dao;

import org.springframework.stereotype.Component;

import edu.cust.rbac.domain.RolePermission;
import edu.cust.util.DAOTemplate;

@Component
public class RolePermissionDAO extends DAOTemplate<RolePermission> {

    public RolePermissionDAO(){
        clazz = RolePermission.class;
        pkColumns = new String[]{"c_role_id", "c_perm_id"};
        comColumns = new String[]{};
        tableName = "c_role_perm";
        init();
    }
}

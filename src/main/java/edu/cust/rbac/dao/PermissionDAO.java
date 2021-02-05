package edu.cust.rbac.dao;

import org.springframework.stereotype.Component;

import edu.cust.rbac.domain.Permission;
import edu.cust.util.DAOTemplate;

@Component
public class PermissionDAO extends DAOTemplate<Permission> {

    public PermissionDAO(){
        clazz = Permission.class;
        pkColumns = new String[]{"c_id"};
        listProjections = new String[]{"c_id", "c_name"};
        comColumns = new String[]{"c_name"};
        tableName = "c_perm";
        init();
    }
}

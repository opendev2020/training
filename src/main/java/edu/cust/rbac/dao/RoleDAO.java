package edu.cust.rbac.dao;

import org.springframework.stereotype.Component;

import edu.cust.rbac.domain.Role;
import edu.cust.util.DAOTemplate;

@Component
public class RoleDAO extends DAOTemplate<Role> {

    public RoleDAO(){
        clazz = Role.class;
        pkColumns = new String[]{"c_id"};
        comColumns = new String[]{"c_name"};
        tableName = "c_role";
        init();
    }
}

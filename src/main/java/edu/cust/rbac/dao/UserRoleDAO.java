package edu.cust.rbac.dao;

import org.springframework.stereotype.Component;

import edu.cust.rbac.domain.UserRole;
import edu.cust.util.DAOTemplate;

@Component
public class UserRoleDAO extends DAOTemplate<UserRole> {
	public UserRoleDAO(){
        clazz = UserRole.class;
        pkColumns = new String[]{"c_username", "c_role_id"};
        comColumns = new String[]{};
        tableName = "c_user_role";
        init();
    }
}

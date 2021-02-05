package edu.cust.rbac.dao;

import org.springframework.stereotype.Component;

import edu.cust.rbac.domain.User;
import edu.cust.util.DAOTemplate;

@Component
public class UserDAO extends DAOTemplate<User> {
    public UserDAO(){
        clazz = User.class;
        pkColumns = new String[]{"c_username"};
        listProjections = new String[]{"c_username", "c_name"};
        comColumns = new String[]{"c_password","c_name"};
        tableName = "c_user";
        init();
    }

}

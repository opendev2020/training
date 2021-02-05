package edu.cust.rbac.dao;

import org.springframework.stereotype.Component;

import edu.cust.rbac.domain.User;
import edu.cust.util.DAOTemplate;

@Component
public class UserDAO2 extends DAOTemplate<User> {
    public UserDAO2(){
        clazz = User.class;
        pkColumns = new String[]{"c_username"};
        comColumns = new String[]{"c_name"};
        tableName = "c_user";
        init();
    }
}

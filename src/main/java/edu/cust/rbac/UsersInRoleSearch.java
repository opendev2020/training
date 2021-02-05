package edu.cust.rbac;

import edu.cust.util.ListTemplate;
import edu.cust.util.search.Search;

public class UsersInRoleSearch extends Search {
	
	@Override
	protected String getTables(ListTemplate lt) {
		// TODO Auto-generated method stub
		return "(select a.* from c_user a left outer join (select c_username from c_user_role where c_role_id=?) b on a.c_username=b.c_username where b.c_username is not null) c";
	}

}

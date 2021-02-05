package edu.cust.rbac;

import edu.cust.util.ListTemplate;
import edu.cust.util.search.Search;

public class PermsNotInRoleSearch extends Search {
	
	@Override
	protected String getTables(ListTemplate lt) {
		// TODO Auto-generated method stub
		return "(select a.* from c_perm a left outer join (select c_perm_id from c_role_perm where c_role_id=?) b on a.c_id=b.c_perm_id where b.c_perm_id is null) c";
	}

}

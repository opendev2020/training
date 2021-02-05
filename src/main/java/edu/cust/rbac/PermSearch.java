package edu.cust.rbac;

import edu.cust.util.search.SimpleSearch;

public class PermSearch extends SimpleSearch {
	
	public PermSearch() {
		columns = new String[]{"c_id", "c_name"};
		operators = new String[] {"like", "like"};
		logicalopts = new String[] {"or", "or"};
		orders = new String[] {"asc", "none"};
	}

}

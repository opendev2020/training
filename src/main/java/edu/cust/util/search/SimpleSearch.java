package edu.cust.util.search;

import edu.cust.util.ListTemplate;
/**
 * 使用该类前端就不用传太多参数，
 * columns,operators,orders,logicalopts等参数都在后端设置，
 * 前端只需要传values参数
 * @author qh
 *
 */
public class SimpleSearch extends Search {

	protected String projections;
	protected String tables;
	@Override
	protected String getTables() {
		// TODO Auto-generated method stub
		return tables;
	}
	@Override
	protected String getProjections() {
		// TODO Auto-generated method stub
		return projections;
	}
	@Override
	public String buildSQL(String whereClause, ListTemplate lt) {
		// TODO Auto-generated method stub
		//检查values数组，如果某个value为空，则将对应的column置为-
		if(values != null && values.length > 0) {
			for(int i = 0; i < values.length; i++) {
				String value = values[i];
				if(value == null || value.equals("")) {
					columns[i] = "-";
				}
			}
		}
		return super.buildSQL(whereClause, lt);
	}
}

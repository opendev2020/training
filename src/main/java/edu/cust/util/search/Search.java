package edu.cust.util.search;

import edu.cust.util.DAOTemplate;
import edu.cust.util.ListTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.regex.Pattern;

/**
 * 使用该类前端必须传的参数有：columns,operators,orders,logicalopts,values
 * 如果感觉前端传的参数太多，可以参考SimpleSearch类
 * @author qh
 *
 */
public class Search {
	protected String[] logicalopts;//逻辑运算符
	protected String[] columns;//列名,如果某列不选择，则此列取值为"-"
	protected String[] operators;//算术运算符
	protected String[] values;//值
	protected String[] orders;//排序
	protected static Map<String, Operator> operatorsMap;
	protected static Set<String> ordersSet;
	protected static Set<String> logicaloptsSet;
	protected static Pattern pattern;//用来验证列名合法性，避免SQL注入，使用正则表达式

	//protected abstract Map<String, ColumnType> getColumnsSet();
	protected String getTables(){
		return null;
	}
	protected String getProjections(){
		return null;
	}
	protected String getTables(ListTemplate lt){
		return lt == null ? getTables() : lt.getTables();
	}
	protected String getProjections(ListTemplate lt){
		return lt == null ? getProjections() : lt.getProjections();
	}
	
	private Logger log = LoggerFactory.getLogger(getClass());
	
	static {
		//算术运算符取值
		operatorsMap = new HashMap<String, Operator>();
		operatorsMap.put("eq", Operator.EQ);
		operatorsMap.put("ne", Operator.NE);
		operatorsMap.put("gt", Operator.GT);
		operatorsMap.put("ge", Operator.GE);
		operatorsMap.put("lt", Operator.LT);
		operatorsMap.put("le", Operator.LE);
		operatorsMap.put("like", Operator.LIKE);
		operatorsMap.put("null", Operator.IS_NULL);
		operatorsMap.put("not_null", Operator.IS_NOT_NULL);
		
		//排序取值
		ordersSet = new HashSet<String>();
		ordersSet.add("none");//不排序
		ordersSet.add("desc");
		ordersSet.add("asc");
		
		//逻辑运算符取值
		logicaloptsSet = new HashSet<String>();
		logicaloptsSet.add("and");
		logicaloptsSet.add("or");

		pattern = Pattern.compile("^\\w+\\.{0,1}\\w+$");
	}
	
	private ColumnType[] cts;
	private Operator[] opts;
	
	public String buildSQL(){
		return buildSQL(null, null);
	}

	public String buildSQL(String whereClause){
		return buildSQL(whereClause, null);
	}
	
	public String buildSQL(ListTemplate lt){
		return buildSQL(null, lt);
	}
	
	public String buildSQL(DAOTemplate<?> dt){
		return buildSQL(null, dt.getLt());
	}
	
	public String buildSQL(String whereClause, ListTemplate lt){
	
		StringBuffer sql = new StringBuffer();
		sql.append("select ");
		sql.append(getProjections(lt));
		sql.append(" from ");
		sql.append(getTables(lt));
	 
		//Map<String, ColumnType> cs = getColumnsSet();
		if(columns != null && columns.length > 0){
			boolean addWhere = false;
			int columnslen = columns.length;
			log.info("columnslen: " + columnslen);
			cts = new ColumnType[columnslen];
			opts = new Operator[columnslen];
			for (int i = 0; i < columnslen; i++) {
				String[] columnArray = columns[i].split("\\|");
				String column = columnArray[0];
				if("-".equals(column)){// 如果不选择搜索列
					continue;
				}
				if(addWhere){
					sql.append(" ");
					String logicalopt = logicalopts[i];
					if(!logicaloptsSet.contains(logicalopt)){
						throw new RuntimeException("logicalopt is invalid: " + logicalopt);
					}
					sql.append(logicalopt);
					sql.append(" ");
				}else{
					addWhere = true;
					sql.append(" where ");
				}
				if(!pattern.matcher(column).find()){
					throw new RuntimeException("column is invalid: " + column);
				}
				ColumnType ct = ColumnTypeFactory.getColumnType(columnArray.length > 1 ? columnArray[1] : null);
				cts[i] = ct;
				//处理like 模糊查询 如果是字符串 需要包裹上%
				if(ct == ColumnType.STRING) {
					if("like".equals(operators[i])) {
						values[i] = "%" + values[i] + "%";
					}
				}
				sql.append(column);
				
				String operator = operators[i];
				Operator opt = operatorsMap.get(operator);
				opts[i] = opt;
				if(opt == null){
					throw new RuntimeException("operator is invalid: " + operator);
				}
				opt.appendSQL(sql);
				
			}
			if(whereClause != null){
				if (addWhere) {
					sql.append(" and " + whereClause);
				} else {
					sql.append(" where " + whereClause);
				}
			}
			boolean addOrder = false;
			for (int i = 0; i < columnslen; i++) {
				if(cts[i] == null){// 如果不选择搜索列
					continue;
				}
				String order = orders[i];
				if(!ordersSet.contains(order)){
					throw new RuntimeException("order is invalid: " + order);
				}
				if("none".equals(order)){
					continue;
				}
				if(addOrder){
					sql.append(", ");
				}else{
					addOrder = true;
					sql.append(" order by ");
				}
				sql.append(columns[i]);
				sql.append(" ");
				sql.append(order);
			}
		}
		
		
		return sql.toString();
	}
	
	
	public ArrayList<Object> getParams(){
		if(cts == null){
			return new ArrayList<Object>();
		}
		int ctslen = cts.length;
		int valueIndex = 0;
		ArrayList<Object> params = new ArrayList<Object>();
		for (int i = 0; i < ctslen; i++) {
			if(cts[i] != null){
				int numOfParams = opts[i].getNumOfParams();
				if(numOfParams == 0){
					valueIndex++;
				}else{
					params.add(cts[i].getValue(values[valueIndex]));
					valueIndex += numOfParams;
				}
			}else{
				valueIndex++;
			}
		}
		return params;
	}
	
	public String[] getLogicalopts() {
		return logicalopts;
	}
	public void setLogicalopts(String[] logicalopts) {
		this.logicalopts = logicalopts;
	}
	public String[] getColumns() {
		return columns;
	}
	public void setColumns(String[] columns) {
		this.columns = columns;
	}
	public String[] getOperators() {
		return operators;
	}
	public void setOperators(String[] operators) {
		this.operators = operators;
	}
	public String[] getValues() {
		return values;
	}
	public void setValues(String[] values) {
		this.values = values;
	}
	public String[] getOrders() {
		return orders;
	}
	public void setOrders(String[] orders) {
		this.orders = orders;
	}
}

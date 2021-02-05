package edu.cust.util.page;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ColumnMapRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;

import edu.cust.util.DAOTemplate;

public abstract class Page {

	protected int pageNum = 1;//页码，从1开始

	protected int recordNum = 20;//每页记录数

	protected int rowCount;//总记录数

	protected int lastPage;

	public abstract String getLimitString(String sql);
	
	public List<Map<String, Object>> getOnePage(String sql, List<?> params, JdbcTemplate jt){
		return getOnePage(sql, params.toArray(), jt);
	}
	
	/**
	 * 使用该方法时，返回的数据实际是List<Map>，
	 * 每一行是一个Map，因为有的时候查询的数据可能是多表连接，没有对应的实体类
	 * @param sql
	 * @param params
	 * @param jt
	 * @return
	 */
	public List<Map<String, Object>> getOnePage(String sql, Object[] params, JdbcTemplate jt){
		setRowCount(sql, params, jt);
		setLastPage();
		if(pageNum > lastPage){
			pageNum = lastPage;
		}
		return jt.query(getLimitString(sql), params, new ColumnMapRowMapper());
	}
	
	/**
	 * 使用该方法时，返回的数据实际是List<Map>，
	 * 每一行是一个Map，因为有的时候查询的数据可能是多表连接，没有对应的实体类
	 * 另外该方法不返回总页数，总记录数等信息，主要方便移动端开发，移动端分页往往不需要这些信息
	 * @param sql
	 * @param params
	 * @param jt
	 * @return
	 */
	public List<Map<String, Object>> getOnePageWithoutRowCount(String sql, Object[] params, JdbcTemplate jt){
		return jt.query(getLimitString(sql), params, new ColumnMapRowMapper());
	}

	public List<?> getOnePage(final String sql, final List<?> params, DAOTemplate<?> dt){
		return getOnePage(sql, params, dt.getLt(), dt.getJt());
	}
	
	public List<?> getOnePage(final String sql, final List<?> params, ResultSetExtractor<List<?>> rse, JdbcTemplate jt){
		return getOnePage(sql, params.toArray(), rse, jt);
	}
	
	public List<?> getOnePage(final String sql, final Object[] params, ResultSetExtractor<List<?>> rse, JdbcTemplate jt){
		setRowCount(sql, params, jt);
		setLastPage();
		if(pageNum > lastPage){
			pageNum = lastPage;
		}
		return getOnePageWithoutRowCount(sql, params, rse, jt);
	}
	
	/**
	 * 该方法不返回总页数，总记录数等信息，主要方便移动端开发，移动端分页往往不需要这些信息
	 * @param sql
	 * @param params
	 * @param rse
	 * @param jt
	 * @return
	 */
	public List<?> getOnePageWithoutRowCount(final String sql, final Object[] params, ResultSetExtractor<List<?>> rse, JdbcTemplate jt){
		return jt.query(new PreparedStatementCreator(){

			public PreparedStatement createPreparedStatement(Connection con)
					throws SQLException {
				// TODO Auto-generated method stub
				PreparedStatement ps = con.prepareStatement(getLimitString(sql), ResultSet.TYPE_SCROLL_INSENSITIVE);
				int pi = 1;
				for (Object param : params) {
					ps.setObject(pi++, param);
				}
				return ps;
			}

		}, rse);
	}
	
	protected int searchFromPosition(String sql){
		Stack<String> selects = new Stack<String>();
		//char[] ca = sql.toCharArray();
		int len = sql.length();
		int wordBorder = 0;
		for(int i = 0; i < len; i++) {
			char c = sql.charAt(i);
			if(c == ' ' || c == '(' || c == ')') {
				String word = sql.substring(wordBorder, i);
				if(word.equals("select")) {
					selects.push(word);
				}else if(word.equals("from")) {
					selects.pop();
					if(selects.isEmpty()) {
						return i;
					}
				}
				wordBorder = i + 1;
			}
		}
		return 0;
	}

	public void setRowCount(String sql, Object[] params, JdbcTemplate jt){
		StringBuffer sqlbuf = new StringBuffer();
		sqlbuf.append("select count(*) from");
		int fi = searchFromPosition(sql);
		//int oi = sql.lastIndexOf("order by");
		sqlbuf.append(sql.substring(fi));//不需要order by
		//System.out.println(sqlbuf.toString());
		jt.query(sqlbuf.toString(), params, new ResultSetExtractor<Object>(){

			public Object extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				// TODO Auto-generated method stub
				if(rs.next()){
					rowCount = rs.getInt(1);
				}
				return null;
			}
			
		});
	}
	
	public int getPrePage(){
		return pageNum <= 1 ? 1 : pageNum - 1;
	}
	
	public int getNextPage(){
		return pageNum >= lastPage ? lastPage : pageNum + 1;
	}
	
	public int getFirstPage(){
		return 1;
	}
	
	public int getLastPage() {
		return lastPage;
	}

	private void setLastPage(){
		if(rowCount == 0){
			lastPage = 1;
			return;
		}
		lastPage = rowCount % recordNum == 0 ? rowCount / recordNum : rowCount / recordNum + 1;
	}
	
	public int getOffset(){
		return (pageNum - 1) * recordNum;
	}
	
	public int getLimit(){
		return pageNum * recordNum;
	}

	public int getRecordNum() {
		return recordNum;
	}

	public void setRecordNum(int recordNum) {
		if(recordNum > 0){
			this.recordNum = recordNum;
		}
	}

	public void setPageNum(int pageNum) {
		if(pageNum > 0){
			this.pageNum = pageNum;
		}
	}

	public int getPageNum() {
		return pageNum;
	}

	public int getRowCount() {
		return rowCount;
	}
	
	public ArrayList<?> getPages(){
		ArrayList<Object> pages = new ArrayList<Object>();
		for (int i = 1; i <= lastPage; i++) {
			pages.add(i);
		}
		return pages;
	}
	
	/**
	 * range最好使用奇数
	 * @param range
	 * @return
	 */
	public ArrayList<?> getPages(int range){
		int end = pageNum + range / 2;
		if(end > lastPage){
			end = lastPage;
		}
		int start = end - range + 1;
		if(start < 1){
			start = 1;
			end = start + range - 1;
			if(end > lastPage){
				end = lastPage;
			}
		}
		ArrayList<Integer> pages = new ArrayList<Integer>();
		for (int i = start; i <= end; i++) {
			pages.add(i);
		}
		return pages;
	}
}

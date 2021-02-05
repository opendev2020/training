package edu.cust.util.page;

public class PageTest extends Page {

	@Override
	public String getLimitString(String sql) {
		// TODO Auto-generated method stub
		return null;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		PageTest pt = new PageTest();
		//String sql = "select a from b where c=d";
		//String sql = "select a from (select b from e) where c=d";
		String sql = "  select  a,(select x from  select1) as z from   (select b from e) where c=d";
		StringBuffer sqlbuf = new StringBuffer();
		sqlbuf.append("select count(*) from");
		int fi = pt.searchFromPosition(sql);
		//int oi = sql.lastIndexOf("order by");
		sqlbuf.append(sql.substring(fi));
		System.out.println(sqlbuf.toString());
		
	}

}

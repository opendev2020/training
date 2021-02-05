package edu.cust.util.excel;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;

public class ExportHeaders {
	private ArrayList<Header> headerArray = new ArrayList<Header>();
	
	private String table;
	
	public ExportHeaders(String table) {
		this.table = table;
	}
	
	public Header addHeader(Header h) {
		headerArray.add(h);
		return h;
	}

	public void writeFirstRow(HSSFSheet sheet){
		HSSFRow row = sheet.createRow((int) 0);
		int cellIndex = 0;
		int rsIndex = 1;
		for (Iterator<Header> i = headerArray.iterator(); i.hasNext();) {
			Header h = i.next();
			if(h.column != null) {
				h.setIndex(rsIndex++);
			}
			if(h.name == null){
				continue;
			}
			HSSFCell cell = row.createCell(cellIndex++);
			cell.setCellValue(h.name);
		}
	}
	
	public void writeRow(HSSFRow row, ResultSet rs) throws SQLException {
		//HSSFRow row = sheet.createRow((int) 0);
		int cellIndex = 0;
		for (Iterator<Header> i = headerArray.iterator(); i.hasNext();) {
			Header h = i.next();
			if(h.name == null){
				continue;
			}
			HSSFCell cell = row.createCell(cellIndex++);
			h.setCellValue(cell, rs);
		}
	}
	
	public String buildSelectSQL(){
		StringBuffer sql = new StringBuffer();
		sql.append("select ");
		for (Iterator<?> i = headerArray.iterator(); i.hasNext();) {
			Header h = (Header) i.next();
			if (h.column != null) {
				sql.append(h.column);
				sql.append(',');
			}
		}
		sql.deleteCharAt(sql.length() - 1);
		sql.append(" from ");
		sql.append(table);
		return sql.toString();
	}
}

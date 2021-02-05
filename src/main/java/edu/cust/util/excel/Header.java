package edu.cust.util.excel;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;

import org.apache.commons.lang3.time.FastDateFormat;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;

public class Header {

	private int index = -1;

	String name;// excel 列名
	String column;// 数据库的 列名
	Object defaultValue;// 默认值
	// HashMap<String, Code> code_dict;//代码字典

	/** 修改后的代码字典 */
	HashMap<String, String> codelib; // 修改后的代码字典

	ColumnTypeConverter converter;// 主要是给导入使用

	private Header associate;

	public Header(String name, String column) {
		this(name, column, null, null, null);
	}

	// 导入使用
	public Header(String name, String column, ColumnTypeConverter converter) {
		this(name, column, null, converter, null);
	}

	public Header(String name, String column, Object defaultValue,
			ColumnTypeConverter converter) {
		this(name, column, defaultValue, converter, null);
	}

	/**
	 * 20131204 修改了代码字典 ljy
	 * 
	 * @param name
	 * @param column
	 * @param default_value
	 * @param converter
	 * @param codeDict
	 */
	public Header(String name, String column, Object defaultValue,
			ColumnTypeConverter converter, HashMap<String, String> codelib) {
		this.name = name;
		this.column = column;
		this.defaultValue = defaultValue;
		this.converter = converter;
		this.codelib = codelib;
	}

	public Header(String name, String column, HashMap<String, String> codelib) {
		this(name, column, null, null, codelib);
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public void setAssociate(Header associate) {
		this.associate = associate;
	}
	
	public String getName() {
		return name;
	}

	public Object getCellValue(HSSFRow row) {
		Object value = null;
		HSSFCell cell = null;
		if (index >= 0) {
			cell = row.getCell(index);
		}
		if (cell != null) {
			value = converter.getValue(cell);
		}

		if (value == null || value.equals("")) {
			if (defaultValue != null) {
				return defaultValue;
			}
			//
			// 一般都是代码列没有值
			// 必须通过名称列反向获取代码值
			if (associate != null) {
				value = associate.getCellValue(row);
			}
			return value;
		}
		if (codelib != null) {
			return codelib.get(value);
		}

		return value;
	}

	static FastDateFormat df = FastDateFormat.getInstance("yyyy-MM-dd");

	public void setCellValue(HSSFCell cell, ResultSet rs) throws SQLException {
		Object value = rs.getObject(index);
		if (value == null) {
			cell.setCellValue("");
			return;
		}
		if (value instanceof Date) {
			cell.setCellValue(df.format((Date) value));
			return;
		}
		if (codelib == null) {
			cell.setCellValue(value.toString());
			return;
		}
		if (value.toString().indexOf(",") < 0) {
			cell.setCellValue(codelib.get(value));
			return;
		}
		// 实现多个代码对应的值在一个单元格输出
		String[] vs = value.toString().split(",");
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < vs.length; i++) {
			String v = vs[i].trim();
			sb.append(codelib.get(v));
			sb.append(",");
		}
		sb.deleteCharAt(sb.length() - 1);
		cell.setCellValue(sb.toString());
	}
}

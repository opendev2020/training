package edu.cust.util.excel;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;


public class Export {
	
	public void exportRecords(HttpServletRequest request, ImportExport ie, final HSSFSheet sheet, JdbcTemplate jt, String whereClause, Object[] params) {
		// 判断一下导出的用到的地方，如果是在"学生导入"->"保存不覆盖用到",那么要做如下处理。
		final ExportHeaders headers = ie.getExportHeaders(request);
		jt.query(headers.buildSelectSQL() + whereClause, params, new ResultSetExtractor<Object>() {
			public Object extractData(ResultSet rs) throws SQLException, DataAccessException {
				exportRecords(headers, sheet, rs);
				return null;
			}
		});
	}

	public void exportRecords(ExportHeaders headers, HSSFSheet sheet, ResultSet rs) throws SQLException {
		// 判断一下导出的用到的地方，如果是在"学生导入"->"保存不覆盖用到",那么要做如下处理。
		
		//ExportHeaders headers = ie.getExportHeaders(request);
		headers.writeFirstRow(sheet);
		
		if(rs == null)
			//rs == null 时导出模板
			return;
		int row_index = 1; //原来row_index = 1，但是这样的话，第一行没有数据。改为从第0行开始填充excel
		
		while (rs.next()) {
			HSSFRow row = sheet.createRow((short) (row_index++));
			headers.writeRow(row, rs);
		}
	}
}

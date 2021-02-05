package edu.cust.util.excel;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.jdbc.core.JdbcTemplate;

import lombok.extern.slf4j.Slf4j;
@Slf4j
public class Import {


	/**
	 * 执行导入的方法
	 * @title importRecords
	 * @date 2013-12-6 上午10:21:03
	 * @param sheet
	 * @param keys
	 * @param jt
	 * @param update
	 * @throws IOException
	 * @throws SQLException void
	 */
	public void importRecords(final HttpServletRequest request, final ImportExport ie, HSSFSheet sheet,
                                      JdbcTemplate jt,
                                      boolean update) throws IOException {
		// 指定导入相对应的sql语句
		ImportHeaders headers = ie.getImportHeaders(request);
		headers.readFirstRow(sheet);
		
		String sql_insert = headers.buildInsertSQL();
		
		/*String sql_update = null;
		if (update) {// 是否覆盖
			sql_update = headers.buildUpdateSQL();
		}*/

		Iterator<Row> i = sheet.rowIterator();
		i.next();
		//ArrayList<Object> keysArray = new ArrayList<Object>();
		for (; i.hasNext(); ) {
			final HSSFRow row = (HSSFRow)i.next();
			//System.out.println("-----"+sql_insert);
			Object[] objs = headers.readRow(row);
			try {
				jt.update(sql_insert, objs);
			}catch(Exception ex) {
				log.warn(Arrays.toString(objs));
				throw ex;
			}
			/*if(!headers.hasPK()){
				
				continue;
			}
			
			Object pk = headers.readPK(row);
			if(pk == null || pk.equals("")){
				continue;
			}
			if (!keys.contains(pk)) {// 如果不存在该主键的信息，就添加进去
				jt.update(sql_insert, headers.readRow(row));
				continue;
			}
			
			keysArray.add(pk);
			if (update) {
				jt.update(sql_update, headers.readRowForUpdate(row));
				continue;
			}*/
		}
		
		/*if(keysArray.isEmpty()){
			return null;
		}
		HSSFWorkbook expWorkbook = new HSSFWorkbook();
		final HSSFSheet expSheet = expWorkbook.createSheet("sheet1");
		final Export exp = new Export();
		
		String sql_select = headers.buildSelectSQL(keysArray.size());
		
		jt.query(sql_select, keysArray.toArray(), new ResultSetExtractor<Object>() {
			public Object extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				ExportHeaders ie.getExportHeaders(request);
				exp.exportRecords(request, ie, expSheet, rs);
				return null;
			}
		});
		if (!update) {
			return expWorkbook;
		}*/
		//return null;
	}
}

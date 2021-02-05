package edu.cust.util.excel;

import org.apache.commons.lang3.time.FastDateFormat;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;

public abstract class ColumnTypeConverter {
	
	private final static DataFormatter DF = new DataFormatter();

	public static final ColumnTypeConverter INT = new ColumnTypeConverter() {

		@Override
		public Object getValue(Cell cell) {
			// TODO Auto-generated method stub
			try {
				return Integer.parseInt(DF.formatCellValue(cell));
			} catch (Exception ex) {
				throw new RuntimeException("整数转换失败：" + cell.getRowIndex() + ":"
						+ cell.getColumnIndex() + ":" + cell.toString());
			}
		}

	};
	public static final ColumnTypeConverter STRING = new ColumnTypeConverter() {

		@Override
		public Object getValue(Cell cell) {
			// TODO Auto-generated method stub
			return DF.formatCellValue(cell);
		}

	};
	
	public static final ColumnTypeConverter LOWER_CASE = new ColumnTypeConverter() {

		@Override
		public Object getValue(Cell cell) {
			// TODO Auto-generated method stub
			String result = DF.formatCellValue(cell);
			return result == null ? null : result.toLowerCase();
		}

	};
	
	public static final ColumnTypeConverter UPPER_CASE = new ColumnTypeConverter() {

		@Override
		public Object getValue(Cell cell) {
			// TODO Auto-generated method stub
			String result = DF.formatCellValue(cell);
			return result == null ? null : result.toUpperCase();
		}

	};

	private final static FastDateFormat DATE_FORMAT = FastDateFormat.getInstance("yyyy-MM-dd");
	public static final ColumnTypeConverter DATE = new ColumnTypeConverter() {

		@Override
		public Object getValue(Cell cell) {
			// TODO Auto-generated method stub
			String result = DF.formatCellValue(cell);
			if(result == null || result.equals("")){
				return null;
			}
			try {
				return DATE_FORMAT.parse(result);
			} catch (Exception ex) {
				throw new RuntimeException("日期转换失败：" + cell.getRowIndex() + ":"
						+ cell.getColumnIndex() + ":" + cell.toString());
			}
		}

	};
	
	public static final ColumnTypeConverter BOOLEAN = new ColumnTypeConverter() {

		@Override
		public Object getValue(Cell cell) {
			// TODO Auto-generated method stub
			String result = DF.formatCellValue(cell);
			if(result == null || result.equals("")){
				return false;
			}
			try {
				return Boolean.parseBoolean(result);
			} catch (Exception ex) {
				throw new RuntimeException("布尔转换失败：" + cell.getRowIndex() + ":"
						+ cell.getColumnIndex() + ":" + cell.toString());
			}
		}

	};

	public abstract Object getValue(Cell cell);

	/*public static void main(String[] args) {
	}*/

}

package edu.cust.util.search;

public class Operator {
	public static final Operator EQ = new Operator();
	public static final Operator NE = new Operator(){

		@Override
		public void appendSQL(StringBuffer sql) {
			// TODO Auto-generated method stub
			sql.append("<>?");
		}
		
	};
	
	public static final Operator GT = new Operator(){
		@Override
		public void appendSQL(StringBuffer sql) {
			// TODO Auto-generated method stub
			sql.append(">?");
		}
	};
	
	public static final Operator GE = new Operator(){
		@Override
		public void appendSQL(StringBuffer sql) {
			// TODO Auto-generated method stub
			sql.append(">=?");
		}
	};
	
	public static final Operator LT = new Operator(){
		@Override
		public void appendSQL(StringBuffer sql) {
			// TODO Auto-generated method stub
			sql.append("<?");
		}
	};
	
	public static final Operator LE = new Operator(){
		@Override
		public void appendSQL(StringBuffer sql) {
			// TODO Auto-generated method stub
			sql.append("<=?");
		}
	};
	
	public static final Operator LIKE = new Operator(){
		@Override
		public void appendSQL(StringBuffer sql) {
			// TODO Auto-generated method stub
			sql.append(" like ? ");
		}
	};
	
	public static final Operator IS_NULL = new Operator(){
		@Override
		public void appendSQL(StringBuffer sql) {
			// TODO Auto-generated method stub
			sql.append(" is null ");
		}

		@Override
		public int getNumOfParams() {
			// TODO Auto-generated method stub
			return 0;
		}
	};
	
	public static final Operator IS_NOT_NULL = new Operator(){
		@Override
		public void appendSQL(StringBuffer sql) {
			// TODO Auto-generated method stub
			sql.append(" is not null ");
		}

		@Override
		public int getNumOfParams() {
			// TODO Auto-generated method stub
			return 0;
		}
	};
	
	protected int numOfParams = 1;
	
	public int getNumOfParams(){
		return numOfParams;
	}
	
	public void appendSQL(StringBuffer sql){
		sql.append("=?");
	}
	
	/*public void setParams(PreparedStatement ps, String[] values, int valueIndex, ColumnType ct, int paramIndex) throws SQLException {
		ps.setObject(paramIndex, ct.getValue(values[valueIndex]));
	}*/
}

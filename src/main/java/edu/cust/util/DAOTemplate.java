package edu.cust.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by qh on 2017/4/11.
 */
public abstract class DAOTemplate<T> {
    protected Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    protected JdbcTemplate jt;

    private Object getObjectFromRs(Method m, ResultSet rs, int i) throws SQLException {
        Class<?> type = m.getParameterTypes()[0];
        Class<?> st = type.getSuperclass();
        if(st == null){//基本数据类型
            char[] narr = type.getName().toCharArray();
            narr[0] = (char)(narr[0] - 32);
            try{
                return ResultSet.class.getMethod("get" + new String(narr), int.class).invoke(rs, i);
            }catch(Exception ex){
                log.info("method name:{}", new String(narr));
                log.warn(ex.getMessage(), ex);
            }
        }
        if(st == Number.class){//基本数据类型的包装类
            String n = type.getSimpleName();
            Object result = null;
            if(n.startsWith("Int")){
                result = rs.getInt(i);
            }else{
                try{
                    result = ResultSet.class.getMethod("get" + n, int.class).invoke(rs, i);
                }catch(Exception ex){
                    log.warn(ex.getMessage(), ex);
                }
            }
            return rs.wasNull() ? null : result;
        }
        if(type == Date.class){
            return rs.getTimestamp(i);
        }
        return rs.getObject(i);
    }

    T wrapResult(ResultSet rs, int startIndex, Method[] pkSetters, Method[] comSetters) throws SQLException {
        try{
            T obj = clazz.newInstance();
            //int i = 1;
            for (Method method: pkSetters) {

                method.invoke(obj, getObjectFromRs(method, rs, startIndex++));
            }
            for (Method method: comSetters) {
                method.invoke(obj, getObjectFromRs(method, rs, startIndex++));
            }
            return obj;
        }catch(Exception ex){
            if(ex instanceof SQLException)
                throw (SQLException) ex;
            throw new RuntimeException("!!!", ex);
        }
    }

    protected T wrapResult(ResultSet rs) throws SQLException{
        return wrapResult(rs, 1, pkSetters, comSetters);
        /*try{
            T obj = clazz.newInstance();
            int i = 1;
            for (Method method: pkSetters) {
                
                method.invoke(obj, getObjectFromRs(method, rs, i++));
            }
            for (Method method: comSetters) {
                method.invoke(obj, getObjectFromRs(method, rs, i++));
            }
            return obj;
        }catch(Exception ex){
            if(ex instanceof SQLException)
                throw (SQLException) ex;
            throw new RuntimeException("!!!", ex);
        }*/
    }

    protected void setIdBeforeInsert(T t) {}

    protected Object[] getInsertParamValues(T t){
        int len = insertPK ? pkColumns.length + comColumns.length : comColumns.length;
        Object[] objects = new Object[len];
        try {
            int i = 0;
            if (insertPK) {
                for (Method method : pkGetters) {
                    objects[i++] = method.invoke(t);
                }
            }
            for (Method method : comGetters) {
                objects[i++] = method.invoke(t);
            }
        }catch(Exception ex){
            throw new RuntimeException("!!!", ex);
        }
        return objects;
    }

    protected Object[] getUpdateParamValues(T t, Object[] pks){
        Object[] objects = new Object[pkColumns.length + comColumns.length + (pks == null ? 0 : pks.length)];
        try {
            int i = 0;
            for (Method method : comGetters) {
                objects[i++] = method.invoke(t);
            }
            for (Method method : pkGetters) {
                objects[i++] = method.invoke(t);
            }
            if(pks != null){
                for (Object pk : pks) {
                    objects[i++] = pk;
                }
            }
        }catch(Exception ex){
            throw new RuntimeException("!!!", ex);
        }
        return objects;
    }

    protected String insertSQL;
    protected String updateSQL;
    protected String updateSqlWithPk;
    protected String loadOneSQL;
    protected String deleteSQL;
    protected String loadMoreSQL;

    protected boolean insertPK = true;
    //protected boolean updatePK = false;
    protected Class<T> clazz;
    private Method[] pkGetters;
    Method[] pkSetters;
    private Method[] comGetters;
    Method[] comSetters;
    protected String[] pkColumns;
    protected String[] comColumns;
    protected String tableName;
    protected String[] listProjections;
    protected ListTemplate lt;

    public ListTemplate getLt() {
        return lt;
    }

    public JdbcTemplate getJt() {
        return jt;
    }

    private String getMethodName(String prefix, String[] components){
        StringBuffer sb = new StringBuffer();
        sb.append(prefix);
        for (int i = 1; i < components.length; i++) {
            String c = components[i];
            sb.append(c.substring(0, 1).toUpperCase());
            sb.append(c.substring(1));
        }
        return sb.toString();
    }

    private void initSettersAndGetters(HashMap<String, Method> settersMap, String[] columns, Method[] setters, Method[] getters){
        try{
            for (int i = 0; i < columns.length; i++) {
                String[] components = columns[i].split("_");
                String methodName = getMethodName("get", components);
                try {
                    getters[i] = clazz.getMethod(methodName);
                }catch(NoSuchMethodException e){
                    methodName = getMethodName("is", components);
                    getters[i] = clazz.getMethod(methodName);
                }
                methodName = getMethodName("set", components);
                setters[i] = settersMap.get(methodName);
            }
        }catch(Exception ex){
            throw new RuntimeException("no method!!!", ex);
        }
    }

    private void initColumns(){
        Method[] methods = clazz.getMethods();
        HashMap<String, Method> settersMap = new HashMap<String, Method>();
        for (Method method: methods) {
            if(method.getName().startsWith("set") && method.getParameterTypes().length == 1){
                settersMap.put(method.getName(), method);
            }
        }
        pkSetters = new Method[pkColumns.length];
        pkGetters = new Method[pkColumns.length];
        initSettersAndGetters(settersMap, pkColumns, pkSetters, pkGetters);
        comSetters = new Method[comColumns.length];
        comGetters = new Method[comColumns.length];
        initSettersAndGetters(settersMap, comColumns, comSetters, comGetters);
    }

    protected void init(){
        initColumns();
        buildSQLs();
        lt = new ListTemplate(){
            {
                tables = tableName;
            }

            @Override
            protected void setSublists() {
                // TODO Auto-generated method stub
                sublists.add(new Sublist(null, DAOTemplate.this, listProjections, null, true));
            }
        };
        lt.init();
    }

    protected void buildSQLs(){
        //构造sql函数
        buildInsertSQL();
        buildUpdateSQL(false);
        buildUpdateSQL(true);
        buildLoadOneSQL();
        buildDeleteSQL();
        buildLoadMoreSQL();
    }

    protected void buildWhereClause(StringBuffer buffer){
        buffer.append(" where ");
        for(int i = 0;i < pkColumns.length;i ++){
            if(i == 0){
                buffer.append(pkColumns[i]);
                buffer.append("=?");
            }else{
                buffer.append(" and ");
                buffer.append(pkColumns[i]);
                buffer.append("=?");
            }
        }
    }

    protected void buildInsertSQL(){
        //生成insertSQL
        StringBuffer buffer = new StringBuffer();
        buffer.append("insert into ");
        buffer.append(tableName);
        buffer.append("(");
        if(insertPK) {
            for (int i = 0; i < pkColumns.length; i++) {
                buffer.append(pkColumns[i]);
                buffer.append(",");
            }
        }
        for(int i = 0;i < comColumns.length;i ++){
            buffer.append(comColumns[i]);
            buffer.append(",");
        }
        buffer.deleteCharAt(buffer.length() - 1);
        buffer.append(") values(");
        int len = insertPK ? pkColumns.length + comColumns.length : comColumns.length;
        for(int i = 0; i < len; i++){
            buffer.append("?,");
        }
        buffer.deleteCharAt(buffer.length() - 1);
        buffer.append(")");
        insertSQL = buffer.toString();

    }
    protected void buildUpdateSQL(boolean updatePk){
        //生成updateSQL
        StringBuffer buffer = new StringBuffer();
        buffer.append("update ");
        buffer.append(tableName);
        buffer.append(" set ");
        for(int i = 0;i < comColumns.length;i ++){
            buffer.append(comColumns[i]);
            buffer.append("=?,");
        }
        if(updatePk){
            for(int i = 0;i < pkColumns.length;i ++){
                buffer.append(pkColumns[i]);
                buffer.append("=?,");
            }
        }
        buffer.deleteCharAt(buffer.length() - 1);
        buildWhereClause(buffer);
        if(updatePk) {
        	updateSqlWithPk = buffer.toString();
        }else {
        	updateSQL = buffer.toString();
        }
    }

    private void addSelectProjections(StringBuffer buffer){
        for (int i = 0; i < pkColumns.length; i++) {
            buffer.append(pkColumns[i]);
            buffer.append(",");
        }
        for(int i = 0; i < comColumns.length; i++){
            buffer.append(comColumns[i]);
            buffer.append(",");
        }
        buffer.deleteCharAt(buffer.length() - 1);
    }

    protected void buildLoadOneSQL(){
        //生成loadSQL
        StringBuffer buffer = new StringBuffer();
        buffer.append("select ");
        addSelectProjections(buffer);
        buffer.append(" from ");
        buffer.append(tableName);
        buildWhereClause(buffer);
        loadOneSQL = buffer.toString();
    }
    protected void buildDeleteSQL(){
        //生成deleteSQL
        StringBuffer buffer = new StringBuffer();
        buffer.append("delete from ");
        buffer.append(tableName);
        buildWhereClause(buffer);
        deleteSQL = buffer.toString();
    }
    protected void buildLoadMoreSQL(){
        //生成getAllSQL
        StringBuffer buffer = new StringBuffer();
        buffer.append("select ");
        addSelectProjections(buffer);
        buffer.append(" from ");
        buffer.append(tableName);
        loadMoreSQL = buffer.toString();
    }
    public void insert(T t) {
        setIdBeforeInsert(t);
        jt.update(insertSQL, getInsertParamValues(t));
    }

    public void update(T t) {
        jt.update(updateSQL, getUpdateParamValues(t, null));
    }

    /**
     * 要修改主键时使用该方法
     * @param t
     * @param oldPks 原主键
     */
    public void updateWithPk(T t, Object[] oldPks) {
        jt.update(updateSqlWithPk, getUpdateParamValues(t, oldPks));
    }

    /**
     * 子句以where或者order by开头
     * @param clause
     * @param i
     * @return
     */
    public ArrayList<T> loadMore(String clause, Object[] i) {
        return jt.query(appendClause(loadMoreSQL, clause), i, new ResultSetExtractor<ArrayList<T>>(){

            public ArrayList<T> extractData(ResultSet rs) throws SQLException,
                    DataAccessException {
                // TODO Auto-generated method stub
                ArrayList<T> result = new ArrayList<T>();
                while(rs.next())
                    result.add(wrapResult(rs));
                return result;
            }

        });
    }

    public T loadOne(Object i) {
        return loadOne(new Object[]{i});
    }


    public T loadOne(Object[] i) {
        return jt.query(loadOneSQL, i, new ResultSetExtractor<T>(){

            public T extractData(ResultSet rs) throws SQLException,
                    DataAccessException {
                // TODO Auto-generated method stub
                if(rs.next())
                    return wrapResult(rs);
                return null;
            }

        });
    }

    /**
     * 
     * @param clause
     * @param i
     * @return
     */
    public T loadOne(String clause, Object[] i) {
    	String sql = appendClause(loadMoreSQL, clause);
    	log.debug("sql: {}", sql);
        return jt.query(sql, i, new ResultSetExtractor<T>(){

            public T extractData(ResultSet rs) throws SQLException,
                    DataAccessException {
                // TODO Auto-generated method stub
                if(rs.next())
                    return wrapResult(rs);
                return null;
            }

        });
    }

    public void delete(Object i){
        delete(new Object[]{i});
    }

    public void delete(Object[] i) {
        jt.update(deleteSQL, i);
    }

    protected String appendClause(String sql, String clause){
        if(clause == null)
            return sql;
        return sql + " " + clause.trim();
    }

}

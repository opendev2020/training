package edu.cust.util;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import javax.annotation.PostConstruct;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * Created by qh on 2017/8/15.
 */

public abstract class ListTemplate implements ResultSetExtractor<List<?>> {


    protected ArrayList<Sublist> sublists = new ArrayList<Sublist>();
    protected HashMap<String, Sublist> sublistsMap = new HashMap<String, Sublist>();
    protected String tables;
    protected String projections;
    protected ArrayList<Object> result;

    public String buildSQL(String clauseAppended){
        StringBuffer sql = new StringBuffer();
        sql.append("select ");
        sql.append(getProjections());
        sql.append(" from ");
        sql.append(getTables());
        if(clauseAppended != null){
            sql.append(" ");
            sql.append(clauseAppended);
        }
        return sql.toString();
    }

    public String getTables() {
        return tables;
    }

    public String getProjections() {
        return projections;
    }

    protected class PK{
        Object[] pks;
        PK(Object[] pks){
            this.pks = pks;
        }

        @Override
        public int hashCode() {
            return Arrays.hashCode(pks);
        }

        @Override
        public boolean equals(Object obj) {
            return Arrays.equals(pks, ((PK)obj).pks);
        }
    }

    protected class Join{
        String targetAlias;
        String setterName;
        String getterName;
        Method setter;
        Method getter;
        boolean isCollection;

        public Join(String targetAlias, String targetField){
            this.targetAlias = targetAlias;
            String name = targetField.substring(0, 1).toUpperCase() + targetField.substring(1);
            setterName = "set" + name;
            getterName = "get" + name;
        }

        void setMethod(Sublist sl){
            try{
                getter = sl.dao.clazz.getMethod(getterName);
                if(getter.getReturnType().isAssignableFrom(Collection.class)){
                    isCollection = true;
                }
                setter = sl.dao.clazz.getMethod(setterName, getter.getReturnType());
            }catch(Exception ex){
                throw new RuntimeException("关联方法名错误:" + getterName, ex);
            }
        }

        void setJoinObject(Object src, Object target){
            try{
                if(isCollection){
                    Object ret = getter.invoke(target);
                    if(ret == null){
                        ret = getter.getReturnType().newInstance();
                        setter.invoke(target, ret);
                    }
                    ((Collection)ret).add(src);
                }else{
                    setter.invoke(target, src);
                }
            }catch(Exception ex){
                throw new RuntimeException("调用方法错误:", ex);
            }
        }

    }


    protected class Sublist{
        String alias;
        DAOTemplate dao;
        Join[] joins;
        boolean ret;
        //String[] projections;

        int startIndex;
        Method[] pkSetters;
        Method[] comSetters;
        StringBuffer projectionsBuffer;
        public Sublist(String alias, DAOTemplate dao, String[] projections, Join[] joins, boolean ret){
            this.alias = alias;
            this.dao = dao;
            this.joins = joins;
            this.ret = ret;
            //this.projections = projections;
            LinkedHashSet<String> pset = new LinkedHashSet<String>();
            if(projections == null){
                projections = dao.comColumns;
            }
            for (String p : projections) {
                pset.add(p);
            }
            projectionsBuffer = new StringBuffer();
            ArrayList<Method> pkSetters = new ArrayList<Method>();
            int i = 0;
            String aliasDot = hasAlias() ? alias + "." : "";
            for (String pkc: dao.pkColumns) {
                projectionsBuffer.append(aliasDot);
                projectionsBuffer.append(pkc);
                projectionsBuffer.append(",");
                pkSetters.add(dao.pkSetters[i++]);
            }
            this.pkSetters = pkSetters.toArray(new Method[pkSetters.size()]);
            ArrayList<Method> comSetters = new ArrayList<Method>();
            i = 0;
            for (String cc: dao.comColumns) {
                if(pset.contains(cc)){
                    projectionsBuffer.append(aliasDot);
                    projectionsBuffer.append(cc);
                    projectionsBuffer.append(",");
                    comSetters.add(dao.comSetters[i]);
                }
                i++;
            }
            this.comSetters = comSetters.toArray(new Method[comSetters.size()]);
        }

        boolean hasAlias(){
            return alias != null && !"".equals(alias);
        }

        Object extractData(ResultSet rs, HashMap<PK, Object> cache) throws SQLException{
            Object[] pks = new Object[pkSetters.length];
            for (int i = 0; i < pks.length; i++){
                pks[i] = rs.getObject(startIndex + i);
            }
            PK pk = new PK(pks);
            Object obj = cache == null ? null : cache.get(pk);
            if(obj != null){
                return obj;
            }
            obj = dao.wrapResult(rs, startIndex, pkSetters, comSetters);
            if(cache != null)
            	cache.put(pk, obj);
            return obj;
            //return null;
        }
    }

    @PostConstruct
    void init(){
        setSublists();
        StringBuffer sql = new StringBuffer();
        int startIndex = 1;
        for (Iterator<Sublist> i = sublists.iterator(); i.hasNext();){
            Sublist sl = i.next();
            if(sl.hasAlias()){
                sublistsMap.put(sl.alias, sl);
            }
            sl.startIndex = startIndex;
            startIndex = startIndex + sl.pkSetters.length + sl.comSetters.length;
            sql.append(sl.projectionsBuffer);
            //sl.dao.pkColumns;
        }
        for (Iterator<Sublist> i = sublists.iterator(); i.hasNext();){
            Sublist sl = i.next();
            if(sl.joins == null){
            	continue;
            }
            for (Join j: sl.joins) {
                j.setMethod(sublistsMap.get(j.targetAlias));
            }
        }
        sql.deleteCharAt(sql.length() - 1);//删除最后一个逗号
        projections = sql.toString();
    }

    protected abstract void setSublists();


    public List<?> extractData(ResultSet resultSet) throws SQLException, DataAccessException {
        HashMap<String, HashMap> cacheMap = new HashMap<String, HashMap>();
        for (Sublist sl: sublists) {
            if(sl.hasAlias()){
                HashMap<PK, Object> cache = new HashMap<PK, Object>();
                cacheMap.put(sl.alias, cache);
            }else{
            }
        }
        ArrayList<Object> result = new ArrayList<Object>();
        
        while(resultSet.next()){
            HashMap<String, Object> row = new HashMap<String, Object>();
            Object[] rowArray = new Object[sublists.size()];
            int i = 0;
            for (Sublist sl: sublists) {
                Object obj = sl.extractData(resultSet, cacheMap.get(sl.alias));
                rowArray[i++] = obj;
                if(sl.hasAlias()){
                    row.put(sl.alias, obj);
                }
                if(sl.ret){
                    result.add(obj);
                }
            }
            i = 0;
            for (Sublist sl: sublists) {
            	if(sl.joins == null){
            		i++;
            		continue;
            	}
                for (Join j: sl.joins) {
                    j.setJoinObject(rowArray[i], row.get(j.targetAlias));
                }
                i++;
            }
        }
        
        return result;
    }
}

package edu.cust.util.search;

import java.util.HashMap;

/**
 * Created by Administrator on 2017/8/31 0031.
 */
public class ColumnTypeFactory {

    static HashMap<String, ColumnType> ctMap = new HashMap<String, ColumnType>();

    static{
        ctMap.put("boolean", ColumnType.BOOLEAN);
        ctMap.put("bool", ColumnType.BOOLEAN);
        ctMap.put("short", ColumnType.SHORT);
        ctMap.put("int", ColumnType.INT);
        ctMap.put("long", ColumnType.LONG);
        ctMap.put("float", ColumnType.FLOAT);
        ctMap.put("double", ColumnType.DOUBLE);
        ctMap.put("date", ColumnType.DATE);
    }

    public static ColumnType getColumnType(String type){
        return type == null ? ColumnType.STRING : ctMap.get(type);
    }
}

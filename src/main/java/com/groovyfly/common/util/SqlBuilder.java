/*
 * SqlBuilder.java
 * 
 * Copyright (c) 2012 Groovy Fly
 */
package com.groovyfly.common.util;

import java.util.ArrayList;
import java.util.List;


/**
 * @author Chris Hatton
 */
public class SqlBuilder {
    
    public static enum Condition {
        AND {
            @Override
            public String toString() {
                return "AND";
            }
        },
        
        OR {
            @Override
            public String toString() {
                return "OR"; 
            }
        },
        
        NONE {
            @Override
            public String toString() {
                return "";
            }
        }
    }
    
    private StringBuilder mainQry = new StringBuilder();
    
    private List<String> filterConditions = new ArrayList<String>();
    
    private String orderByCond = "";
    
    private SqlBuilder(String mainQry) {
        super();
        this.mainQry.append(mainQry);
    }
    
    public static SqlBuilder getBuilder(String mainQry) {
        return new SqlBuilder(mainQry);
    }
    
    public SqlBuilder addEquals(Condition cond, String column, int value) {
        filterConditions.add(cond + " " + column + " = " + value);    
        
        return this;
    }
    
    public SqlBuilder addBeginsLike(Condition cond, String column, String value) {
        filterConditions.add(cond + " " + column + " LIKE '" + DbUtil.addSlashes(value) + "%'");    
        
        return this;
    }
    
    public SqlBuilder addIn(Condition cond, String column, int... i) {
        filterConditions.add(cond + " " + column + " IN (" + getInValue(i) + ")");
        
        return this;
    }
    
    public SqlBuilder addNotIn(Condition cond, String column, int... i) {
        filterConditions.add(cond + " " + column + " IN (" + getInValue(i) + ")");
        
        return this;
    }
    
    public SqlBuilder addOpenBracket() {
        filterConditions.add("(");
        
        return this;
    }
    
    public SqlBuilder addCloseBracket() {
        filterConditions.add(")");
        
        return this;
    }
    
    public String build() {
        mainQry.append(" AND 1 = 1 ");
        for (String conString : filterConditions) {
            mainQry.append(" ");
            mainQry.append(conString);
        }
        mainQry.append(orderByCond);
        
        return mainQry.toString();
    }
    
    private String getInValue(int... i) {
        StringBuilder sb = new StringBuilder();
        
        for (int j : i) {
            if (j > 0) {
                sb.append(" " + j + ", ");                
            }
        }

        String toReturn = "";
        if (sb.length() > 0) {
            toReturn = sb.substring(0, sb.lastIndexOf(",") - 1);
        }
        
        return toReturn;
    }

}

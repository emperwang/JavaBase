package com.wk.Config;

/**
 * 包含解析的mapper.xml文件中的属性
 */
public class Function {
    private String sqlType;  //sql的类型,insert select and so on
    private String functionName; //方法名, 也就是id
    private String sql;         //执行的sql语句
    private Object resultType;  //返回类型
    private String parameterType;   //参数类型

    public String getSqlType() {
        return sqlType;
    }

    public void setSqlType(String sqlType) {
        this.sqlType = sqlType;
    }

    public String getFunctionName() {
        return functionName;
    }

    public void setFunctionName(String functionName) {
        this.functionName = functionName;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public Object getResultType() {
        return resultType;
    }

    public void setResultType(Object resultType) {
        this.resultType = resultType;
    }

    public String getParameterType() {
        return parameterType;
    }

    public void setParameterType(String parameterType) {
        this.parameterType = parameterType;
    }
}

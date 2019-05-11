package com.wk.Config;

import java.util.List;

// 解析的mapper接口文件
public class MapperBean {
    private String interfaceName;//接口名
    private List<Function> list; //接口下所有的方法

    public String getInterfaceName() {
        return interfaceName;
    }

    public void setInterfaceName(String interfaceName) {
        this.interfaceName = interfaceName;
    }

    public List<Function> getList() {
        return list;
    }

    public void setList(List<Function> list) {
        this.list = list;
    }
}

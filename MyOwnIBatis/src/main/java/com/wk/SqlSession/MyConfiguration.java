package com.wk.SqlSession;

import com.wk.Config.Function;
import com.wk.Config.MapperBean;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 读取与解析配置信息，并返回处理后的Enviroment
 */
public class MyConfiguration {
    private static ClassLoader loader = ClassLoader.getSystemClassLoader();

    /**
     *  读取config.xml信息并处理
     *  在这里主要是用于获取数据库的连接信息
     * @param resource
     * @return
     */
    public Connection build(String resource){
        try {
            InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream(resource);
            SAXReader saxReader = new SAXReader();
            Document document = saxReader.read(resourceAsStream);
            Element rootElement = document.getRootElement();
            return evalDataSource(rootElement);
        } catch (DocumentException e) {
            throw new RuntimeException("Error occured while evaling xml:"+resource);
        }
    }

    private Connection evalDataSource(Element node) {
        if (!node.getName().equals("database")){
            throw new RuntimeException("root should be <database>");
        }

        String driverClassName = null;
        String url = null;
        String username = null;
        String password = null;
        //获取属性节点
        for (Object item:node.elements("property")){
            Element item1 = (Element) item;
            String value = getValue(item1);
            String name = item1.attributeValue("name");
            if (name == null || value == null){
                throw new RuntimeException("<database>:<property> should caontain name and value");
            }

            switch (name){
                case "url": url = value;break;
                case "username": username = value;break;
                case "password": password = value;break;
                case "driverClassName": driverClassName = value;break;
                default: throw new RuntimeException("<database>:<property> unknown name");
            }
        }
        Connection connection = null;
        try {
            Class.forName(driverClassName);
            connection = DriverManager.getConnection(url,username,password);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

    //读取property属性的值，如果拥有value值，则读取，没有设置value则读取内容
    private String getValue(Element item1) {
        return item1.hasContent() ? item1.getText() : item1.attributeValue("value");
    }
    // 这里是读取mapper.xml配置文件
    public MapperBean readMapper(String path){
        MapperBean mapper = new MapperBean();
        try{
            InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream(path);
            SAXReader reader = new SAXReader();
            Document document = reader.read(resourceAsStream);
            Element rootElement = document.getRootElement();
            mapper.setInterfaceName(rootElement.attributeValue("nameSpace").trim());//把nameSpace存储为接口名
            List<Function> list = new ArrayList<>(); //用来存储方法
            //遍历根节点下的所有子节点, 也就是获取所有方法
            for (Iterator iterator = rootElement.elementIterator();iterator.hasNext();){
                Function function = new Function();
                Element next = (Element) iterator.next();
                String sqlType = next.getName().trim();
                String functionName = next.attributeValue("id").trim();
                String sql = next.getText().trim();
                String resultType = next.attributeValue("resultType").trim();
                //String parameterType = next.attributeValue("parameterType").trim();
                function.setSqlType(sqlType);
                function.setSql(sql);
                function.setFunctionName(functionName);
                //function.setParameterType(parameterType);

                Object newInstance = null;
                newInstance = Class.forName(resultType).newInstance();
                function.setResultType(newInstance);
                list.add(function);
            }
            mapper.setList(list);
        }catch (Exception e){
            e.printStackTrace();
        }
        return mapper;
    }
}

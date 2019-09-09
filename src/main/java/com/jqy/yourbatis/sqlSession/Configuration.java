package com.jqy.yourbatis.sqlSession;

import com.jqy.yourbatis.config.Mapper;
import com.jqy.yourbatis.config.MapperMethod;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.*;

public class Configuration {

    private String driverClassName;
    private String databaseUrl;
    private String userName;
    private String password;

    private Map<String, String> properties=new HashMap<>();

    private void init(Element node){
        Map<String, String> tempMap=new HashMap<>();
        if (!node.getName().equals("database"))
            throw new RuntimeException("Couldn't find [database]");
        List<Element> props=node.elements("property");
        for (Element prop:props){
            String name=prop.attributeValue("name");
            String value=prop.getText();
            tempMap.put(name,value);
        }
        if (properties == null) {
            throw new RuntimeException("Configuration error!");
        }
        this.properties=tempMap;
    }

    private static ClassLoader loader= ClassLoader.getSystemClassLoader();

    public Connection getConnection(String resource){
        try {
            InputStream stream=loader.getResourceAsStream(resource);
            SAXReader reader=new SAXReader();
            Document document=reader.read(stream);
            Element root=document.getRootElement();
            return getConnectionFromDataSource(root);
        }catch (Exception e){
            throw new RuntimeException("Error when handling "+resource);
        }
    }

    private Connection getConnectionFromDataSource(Element node) throws ClassNotFoundException {
        init(node);
        this.driverClassName=properties.get("driverClassName");
        this.password=properties.get("password");
        this.userName=properties.get("username");
        this.databaseUrl=properties.get("url");
        Class.forName(this.driverClassName);
        Connection connection=null;
        try {
            connection= DriverManager.getConnection(databaseUrl,userName,password);
        }catch (SQLException e){
            System.out.println("Connection error!");
            e.printStackTrace();
        }
        return connection;
    }

    public Mapper readMapper(String resource){
        Mapper mapper=new Mapper();
        try {
            InputStream inputStream=loader.getResourceAsStream(resource);
            SAXReader reader=new SAXReader();
            Document document=reader.read(inputStream);
            Element root=document.getRootElement();
            mapper.setInterfaceName(root.attributeValue("nameSpace").trim());
            List<MapperMethod> list=new ArrayList<>();
            for (Iterator iterator = root.elementIterator(); iterator.hasNext();){
                MapperMethod method=new MapperMethod();
                Element e=(Element)iterator.next();
                String type=e.getName().trim();
                String methodName=e.attributeValue("id").trim();
                String sql=e.getText().trim();
                String resultType=e.attributeValue("resultType").trim();
                method.setMethodName(methodName);
                method.setType(type);
                Object instance=null;
                try {
                    instance= Class.forName(resultType).newInstance();
                }catch (Exception error){
                    error.printStackTrace();
                }
                method.setResultType(instance);
                method.setSql(sql);
                list.add(method);
            }
            mapper.setMethods(list);
        }catch (Exception e){
            e.printStackTrace();
        }
        return mapper;
    }
}

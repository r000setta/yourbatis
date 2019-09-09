package com.jqy.yourbatis.sqlSession;

import com.jqy.yourbatis.executor.Executor;
import com.jqy.yourbatis.executor.MyExecutor;

import java.lang.reflect.Proxy;

public class SqlSession {
    private Executor excutor=new MyExecutor();

    private Configuration configuration=new Configuration();

    public <T> T selectOne(String statement, Object parameter){
        return excutor.query(statement,parameter);
    }

    @SuppressWarnings("unchecked")
    public <T> T getMapper(Class<T> tClass){
        return (T) Proxy.newProxyInstance(tClass.getClassLoader(),new Class[]{tClass},
                new MapperProxy(configuration,this));
    }

}

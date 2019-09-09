package com.jqy.yourbatis.sqlSession;

import com.jqy.yourbatis.config.Mapper;
import com.jqy.yourbatis.config.MapperMethod;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.List;

public class MapperProxy implements InvocationHandler {

    private SqlSession sqlSession;

    private Configuration configuration;

    public MapperProxy(Configuration configuration,SqlSession sqlSession){
        this.configuration=configuration;
        this.sqlSession=sqlSession;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Mapper mapper=configuration.readMapper("PersonMapper.xml");
        if (!method.getDeclaringClass().getName().equals(mapper.getInterfaceName())){
            return null;
        }
        List<MapperMethod> list=mapper.getMethods();
        if (null!=list||list.size()!=0){
            for(MapperMethod mapperMethod:list){
                if (method.getName().equals(mapperMethod.getMethodName())){
                    return sqlSession.selectOne(mapperMethod.getSql(), String.valueOf(args[0]));
                }
            }
        }
        return null;
    }
}

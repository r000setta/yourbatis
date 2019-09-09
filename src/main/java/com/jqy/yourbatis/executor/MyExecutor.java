package com.jqy.yourbatis.executor;

import com.jqy.yourbatis.bean.Person;
import com.jqy.yourbatis.cache.CacheKey;
import com.jqy.yourbatis.cache.PerpetualCache;
import com.jqy.yourbatis.sqlSession.Configuration;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MyExecutor implements Executor {
    private PerpetualCache localCache;

    private boolean closed;

    public MyExecutor(){
        this.localCache=new PerpetualCache("localCache");
    }
    private Configuration xmlConfiguration=new Configuration();

    @Override
    public <T> T query(String sql, Object parameter) {
        CacheKey cacheKey=createCacheKey(sql);
        Object value=localCache.getObject(cacheKey);
        if (value!=null){
            return (T)value;
        }
        Connection connection=getConnction();
        ResultSet set=null;
        PreparedStatement preparedStatement=null;
        try {
            preparedStatement=connection.prepareStatement(sql);
            preparedStatement.setString(1,parameter.toString());
            set=preparedStatement.executeQuery();
            Person person=new Person();
            while (set.next()){
                person.setId(set.getInt(1));
                person.setUsername(set.getString(2));
                person.setNote(set.getString(3));
            }
            localCache.putObject(cacheKey,person);
            return (T)person;
        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            try {
                if (set!=null)
                    set.close();
                if (preparedStatement!=null)
                    preparedStatement.close();
                if (connection!=null)
                    connection.close();

            }catch (Exception exp){
                exp.printStackTrace();
            }
        }
        return null;
    }

    private Connection getConnction(){
        try {
            Connection connection=xmlConfiguration.getConnection("config.xml");
            return connection;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public CacheKey createCacheKey(String sql){
        if (closed)
            throw new RuntimeException("Executor was closed");
        CacheKey cacheKey=new CacheKey();
        cacheKey.update(sql);
        return cacheKey;
    }
}

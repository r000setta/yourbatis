package com.jqy.yourbatis;

import com.jqy.yourbatis.bean.Person;
import com.jqy.yourbatis.mapper.PersonMapper;
import com.jqy.yourbatis.sqlSession.SqlSession;

public class Test {
    public static void main(String[] args) {
        SqlSession sqlsession=new SqlSession(); //
        PersonMapper mapper = sqlsession.getMapper(PersonMapper.class);
        Person user = mapper.getPersonById(1);
        System.out.println(user.toString());
    }
}

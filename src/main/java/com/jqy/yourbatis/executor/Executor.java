package com.jqy.yourbatis.executor;

public interface Executor {
    <T> T query(String statement, Object parameter);
}

package com.jqy.yourbatis.cache;

public interface Cache {

    void putObject(Object key, Object value);

    Object getObject(Object key);

    Object removeObject(Object key);

    void clear();

    int getSize();
}

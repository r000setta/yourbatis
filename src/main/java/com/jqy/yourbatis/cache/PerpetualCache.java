package com.jqy.yourbatis.cache;

import java.util.HashMap;
import java.util.Map;

public class PerpetualCache implements Cache {

    private final String id;

    public PerpetualCache(String id){
        this.id=id;
    }

    private Map<Object, Object> cache=new HashMap<>();

    @Override
    public void putObject(Object key, Object value) {
        cache.put(key,value);
    }

    @Override
    public Object getObject(Object key) {
        return cache.get(key);
    }

    @Override
    public Object removeObject(Object key) {
        return cache.remove(key);
    }

    @Override
    public void clear() {
        cache.clear();
    }

    @Override
    public int getSize() {
        return cache.size();
    }
}

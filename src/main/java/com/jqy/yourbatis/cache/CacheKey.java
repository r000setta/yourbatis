package com.jqy.yourbatis.cache;

import com.jqy.yourbatis.reflection.ArrayUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

public class CacheKey implements Cloneable, Serializable {
    private static final long serialVersionUID = 876323262645176354L;

    private static final int DEFAULT_MULTIPLYER=37;
    private static final int DEFAULT_HASHCODE=17;

    private final int multiplier;
    private int hashcode;
    private long checksum;
    private int count;
    private List<Object> updateList;

    public CacheKey(){
        this.hashcode=DEFAULT_HASHCODE;
        this.multiplier=DEFAULT_HASHCODE;
        this.count=0;
        this.updateList=new ArrayList<>();
    }

    public int getUpdateCount(){
        return updateList.size();
    }

    public void update(Object object){
        int baseHashCode=object==null ? 1 : ArrayUtil.hashCode(object);
        count++;
        checksum+=baseHashCode;
        baseHashCode*=count;
        hashcode=multiplier*hashcode+baseHashCode;
        updateList.add(object);
    }

    public void updateAll(Object[] objects){
        for (Object o:objects){
            update(o);
        }
    }

    @Override
    public boolean equals(Object obj) {
        if(this==obj)
            return true;
        if (!(obj instanceof CacheKey))
            return false;
        final CacheKey cacheKey=(CacheKey)obj;

        if(hashcode!=cacheKey.hashcode || checksum!=cacheKey.checksum ||
        count!=cacheKey.count)
            return false;
        for (int i=0;i<updateList.size();i++){
            Object thisObj=updateList.get(i);
            Object thatObj=((CacheKey) obj).updateList.get(i);
            if (!ArrayUtil.equals(thisObj,thatObj))
                return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        return hashcode;
    }

    @Override
    public String toString() {
        StringJoiner value=new StringJoiner(":");
        value.add(String.valueOf(hashcode));
        value.add(String.valueOf(checksum));
        updateList.stream().map(ArrayUtil::toString).forEach(value::add);
        return value.toString();
    }
}

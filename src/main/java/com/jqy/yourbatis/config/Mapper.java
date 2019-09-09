package com.jqy.yourbatis.config;

import java.util.List;

public class Mapper {
    private String interfaceName;

    private List<MapperMethod> methods;

    public String getInterfaceName() {
        return interfaceName;
    }

    public void setInterfaceName(String interfaceName) {
        this.interfaceName = interfaceName;
    }

    public List<MapperMethod> getMethods() {
        return methods;
    }

    public void setMethods(List<MapperMethod> methods) {
        this.methods = methods;
    }
}

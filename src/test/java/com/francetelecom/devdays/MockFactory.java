package com.francetelecom.devdays;

import java.util.HashMap;
import java.util.Map;

import org.mockito.Mockito;
import org.springframework.beans.factory.FactoryBean;

public class MockFactory implements FactoryBean {

	private static Map<Class<?>,Object> typeMap = new HashMap<Class<?>,Object>();

    private Class<?> type;// the created object type

    public void setType(final Class<?> type) {
        this.type = type;
    }

    @Override
    public Object getObject() throws Exception {
        if(!typeMap.containsKey(type)) {
            typeMap.put(type,Mockito.mock(type));
        }

        return typeMap.get(type);
    }

    @Override
    public Class<?> getObjectType() {
        return type;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

}

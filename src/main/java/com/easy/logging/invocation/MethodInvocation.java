package com.easy.logging.invocation;

import com.easy.logging.Invocation;

import java.lang.reflect.Method;
import java.util.HashMap;

public abstract class MethodInvocation implements Invocation {
    private HashMap<String,Object> attributeMap = new HashMap<>();
    private final Class<?> target;
    private final Method method;
    private final Object[] argurments;


    public MethodInvocation(Class<?> target, Method method, Object[] argurments){
        this.target = target;
        this.method = method;
        this.argurments = argurments;

    }
    @Override
    public Method getMethod() {
        return method;
    }

    @Override
    public Class<?> getTarget() {
        return target;
    }

    @Override
    public Object[] getArgurments() {
        return argurments;
    }

    @Override
    public Object getAttribute(String key) {
        return attributeMap.get(key);
    }


    @Override
    public void setAttribute(String key, Object value) {
        attributeMap.put(key,value);
    }

}

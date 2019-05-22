package com.easy.logging.invocation.adapter;

import com.easy.logging.Invocation;

import java.lang.reflect.Method;
import java.util.HashMap;

public abstract class InvocationAdapter implements Invocation {

    private HashMap<String,Object> attributeMap = new HashMap<>();
    private  Class<?> target;
    private  Method method;
    private  Object[] argurments;

    protected void setTarget(Class<?> target) {
        this.target = target;
    }

    protected void setMethod(Method method) {
        this.method = method;
    }

    protected void setArgurments(Object[] argurments) {
        this.argurments = argurments;
    }

    @Override
    public Object getAttribute(String key) {
        return attributeMap.get(key);
    }


    @Override
    public void setAttribute(String key, Object value) {
        attributeMap.put(key,value);
    }


    @Override
    public Class<?> getTarget() {
        return target;
    }

    @Override
    public Method getMethod() {
        return method;
    }

    @Override
    public Object[] getArgurments() {
        return argurments;
    }






}

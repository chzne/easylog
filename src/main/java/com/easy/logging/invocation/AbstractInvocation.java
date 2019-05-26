package com.easy.logging.invocation;

import com.easy.logging.Invocation;
import com.easy.logging.InvocationLogger;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;

public abstract class AbstractInvocation implements Invocation {

    private HashMap<String,Object> attributeMap = new HashMap<>();

    private  Class<?> target;
    private  Method method;
    private  Object[] argurments;

    protected long startTime;

    protected long endTime;

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


    @Override
    public Object proceed() throws Throwable {
        try {
            startTime=System.currentTimeMillis();
            Object object = doInvoke();
            return object;

        }finally {
            endTime=System.currentTimeMillis();
        }

    }

    public abstract Object doInvoke() throws Throwable ;

    @Override
    public long getStartTime() {
        return startTime;
    }


    @Override
    public long getEndTime() {
        return endTime;
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

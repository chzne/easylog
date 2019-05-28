package com.easy.logging;

import java.lang.reflect.Method;

public interface Invocation {


    Object proceed() throws Throwable;



    public long getStartTime();

    public long getEndTime();

    public Method getMethod();

    public Class<?> getTarget();

    public Object[] getArgurments();

    public Object getAttribute(String key);

    public void setAttribute(String key, Object value);
}

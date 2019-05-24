package com.easy.logging;

import org.springframework.lang.Nullable;

import java.lang.reflect.Method;

public interface Invocation {


    Object proceed() throws Throwable;



    public long getStartTime();

    public long getEndTime();

    @Nullable
    public Method getMethod();


    public Class<?> getTarget();

    @Nullable
    public Object[] getArgurments();

    public Object getAttribute(String key);

    public void setAttribute(String key, Object value);
}

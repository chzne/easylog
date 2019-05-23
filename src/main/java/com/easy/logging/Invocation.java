package com.easy.logging;

import org.springframework.lang.Nullable;

import java.lang.reflect.Method;

public interface Invocation {

    public static final String START_TIME_ATTRIBUTE_KEY="start_time_attribute_key";

    public static final String END_TIME_ATTRIBUTE_KEY="end_time_attribute_key";

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

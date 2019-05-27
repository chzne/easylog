package com.easy.logging;

import java.util.LinkedList;

public interface Session {
    public String getSessionId();

    public Trace getTrace();

    public void setTrace(Trace trace);

    public boolean isStarted();

    public void start(Invocation invocation);

    public boolean isHeadInvocation(Invocation invocation);

    public Invocation getHeadInvocation();

    public LinkedList<Invocation> getInvocations();

    public Object getAttribute(String key);

    public void setAttribute(String key, Object value);

    public void destory(Invocation invocation);

    public boolean isDestoried();
}

package com.easy.logging;

import java.util.LinkedList;
import java.util.Map;

public interface Session {

    public String getSessionId();

    public Trace getTrace();

    public long getCreatedTime();

    public long getLastStartedTime();

    public long getLastStoppedTime();

    public void setTrace(Trace trace);

    public boolean isStarted();

    public boolean isInProcess();

    public void start(Invocation invocation);

    public void stop(Invocation invocation);

    public boolean isHeadInvocation(Invocation invocation);

    public Invocation getHeadInvocation();

    public LinkedList<Invocation> getInvocations();

    public Object getAttribute(String key);

    public Map<String,Object> getAttributes();

    public void setAttribute(String key, Object value);

    public void close(Invocation invocation);

    public boolean isClosed();

    public boolean isStopped();
}

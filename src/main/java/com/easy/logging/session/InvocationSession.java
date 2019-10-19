package com.easy.logging.session;

import com.easy.logging.*;
import com.easy.logging.Trace;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

public class InvocationSession implements Session {



    public final static String DEFAULT_TRACE_MDC_PARAMETER_NAME = "traceId";

    protected Map<String,Object>  attributeMap = new ConcurrentHashMap<>();

    protected Trace trace;

    protected boolean started=false;

    protected boolean closed=false;

    protected Random random = new Random();

    protected String sessionId;

    protected Invocation headInvocation;

    protected long createdTime;

    protected long lastStartedTime;

    protected long lastStoppedTime;

    protected long closedTime;

    protected boolean inProcess=false;

    private boolean stopped=false;

    @Override
    public String getSessionId() {
        return sessionId;
    }

    @Override
    public Trace getTrace() {
        return trace;
    }

    @Override
    public long getCreatedTime() {
        return createdTime;
    }

    @Override
    public long getLastStartedTime() {
        return lastStartedTime;
    }

    @Override
    public long getLastStoppedTime() {
        return lastStoppedTime;
    }

    @Override
    public void setTrace(Trace trace) {
        this.trace = trace;
    }

    @Override
    public boolean isStarted() {
        return started;
    }

    @Override
    public boolean isInProcess() {
        return inProcess;
    }


    @Override
    public void start(Invocation invocation) {
        if(closed || started){
            return;
        }
        this.headInvocation = invocation;
        createdTime=System.currentTimeMillis();
        started=true;
        inProcess= true;
    }

    @Override
    public void stop(Invocation invocation) {
        if(!stopped){
            stopped=true;
            lastStoppedTime=System.currentTimeMillis();
            inProcess=false;
        }

    }




    @Override
    public boolean isHeadInvocation(Invocation invocation) {
        if(invocation==headInvocation){
            return true;
        }
        return false;

    }

    @Override
    public Invocation getHeadInvocation() {
        return headInvocation;
    }

    @Override
    public LinkedList<Invocation> getInvocations() {
        return null;
    }

    @Override
    public Object getAttribute(String key) {
        return attributeMap.get(key);

    }

    @Override
    public void setAttribute(String key, Object value) {
         attributeMap.put(key,value);
    }


    public Map<String,Object> getAttributes() {
        return attributeMap;
    }

    @Override
    public void close(Invocation invocation) {

        if(!closed){
            stopped=true;
            closed=true;
            closedTime=System.currentTimeMillis();
        }
    }

    @Override
    public boolean isClosed() {
        return closed;
    }

    @Override
    public boolean isStopped() {
        return stopped;
    }


}

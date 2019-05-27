package com.easy.logging.session;

import com.easy.logging.*;
import com.easy.logging.Trace;

import java.util.LinkedList;
import java.util.Random;

public class DistributedSession implements Session {



    public final static String DEFAULT_TRACE_MDC_PARAMETER_NAME = "traceId";



    protected Trace trace;

    protected boolean started=false;

    protected Random random = new Random();

    protected String sessionId;

    protected Invocation headInvocation;



    @Override
    public String getSessionId() {
        return sessionId;
    }

    @Override
    public Trace getTrace() {
        return trace;
    }

    @Override
    public void setTrace(Trace trace) {
        this.trace = trace;
    }



    @Override
    public boolean isStarted() {
        return false;
    }

    @Override
    public void start(Invocation invocation) {
        if(started){
            return;
        }
        this.headInvocation = invocation;
        started=true;


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
        return null;
    }

    @Override
    public void setAttribute(String key, Object value) {

    }

    @Override
    public void destory(Invocation invocation) {

    }

    @Override
    public boolean isDestoried() {
        return false;
    }


}

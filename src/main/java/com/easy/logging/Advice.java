package com.easy.logging;
/**
 *
 * it will be call by InvocationDelegator on different stage of invocation
 * the main purpose of Advice Interface is to decouple the dependencies between modules
 * such as Log,Trace,Notify,Monitor etc
 * so we use it to  make every single module independent
 *
 * */
public interface Advice {
    /**
     * get called before invocation of a method
     * */
    public void before(Invocation invocation);
    /**
     * get called after invocation of a method
     * */
    public void after(Invocation invocation,Object result);
    /**
     * get called when exception occurred in a invocation
     * */
    public void throwing(Invocation invocation,Throwable throwable);
}

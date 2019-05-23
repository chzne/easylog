package com.easy.logging;


/**
 * it's a core interface
 * its subclasses will invokes the user method and invoke the method of a advice on a different stage of invocation
 * */
public interface InvocationDelegator {



    public Object invoke(Invocation invocation) throws Throwable;
}

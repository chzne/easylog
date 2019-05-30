package com.easy.logging;


import com.easy.logging.invocation.adapter.InvocationAdapter;

/**
 * it's a core interface
 * its subclasses will invokes the user method and invoke the method of a processor on a different stage of invocation
 * */
public interface InvocationProxy {

    public Object invoke(InvocationAdapter invocation) throws Throwable;


}

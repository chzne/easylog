package com.easy.logging;


import java.util.List;

/**
 * it's a core interface
 * its subclasses will invokes the user method and invoke the method of a advice on a different stage of invocation
 * */
public interface InvocationDelegator {

    public List<Advice> getAdvices();

    public Object invoke(Invocation invocation) throws Throwable;
}

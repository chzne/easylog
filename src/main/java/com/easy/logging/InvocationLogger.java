package com.easy.logging;

public interface InvocationLogger<T extends Invocation> {

    public void before(T invocation);

    public void after(T invocation,Object result);

    public void throwing(T invocation,Throwable throwable);
}

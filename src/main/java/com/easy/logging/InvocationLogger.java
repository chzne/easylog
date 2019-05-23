package com.easy.logging;

public interface InvocationLogger<T extends Invocation>   {


    public int getPriority(Class<? extends Invocation> invocation);

    public void before(T invocation);

    public void after(T invocation,Object result);

    public void throwing(T invocation,Throwable throwable);
}

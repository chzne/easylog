package com.easy.logging;

public interface LoggerRegistry {

    public InvocationLogger getLoggerWithHighPriority(Class<? extends Invocation> invocation);

    public void registor(InvocationLogger logger);

}

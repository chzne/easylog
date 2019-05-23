package com.easy.logging;

import java.util.List;

public interface LoggerRegistry {

    public InvocationLogger getLoggerWithHighPriority(Class<? extends Invocation> invocation);

    public void registor(InvocationLogger logger);

}

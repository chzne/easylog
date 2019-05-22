package com.easy.logging;

import com.easy.logging.logging.logger.CompositeInvocationLogger;

import java.util.List;

public interface LoggerRegistry {

    public void registor(Class<? extends Invocation> invocation, InvocationLogger logger);

    public List<InvocationLogger> getLoggers(Class<? extends Invocation> invocation);

    public CompositeInvocationLogger getCompositeLogger(Class<? extends Invocation> invocation);
}

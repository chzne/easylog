package com.easy.logging;

import java.util.List;

public interface LoggerRegistry {

    public void register(InvocationLogger logger);

    public List<? extends InvocationLogger> getLoggersByType(Class<? extends Invocation> clazz);

    public List<? extends InvocationLogger> getLoggers();


}

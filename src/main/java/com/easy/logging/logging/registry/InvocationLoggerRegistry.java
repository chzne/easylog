package com.easy.logging.logging.registry;

import com.easy.logging.LoggerRegistry;
import com.easy.logging.logging.logger.CompositeInvocationLogger;
import com.easy.logging.Invocation;
import com.easy.logging.InvocationLogger;

import java.util.HashMap;
import java.util.List;

public class InvocationLoggerRegistry implements LoggerRegistry {

    protected HashMap<Class<? extends Invocation>, CompositeInvocationLogger> loggerHashMap = new HashMap<>();





    @Override
    public void registor(Class<? extends Invocation> invocationCls, InvocationLogger logger) {
        CompositeInvocationLogger composite = loggerHashMap.get(invocationCls);
        if(composite==null){
            composite = new CompositeInvocationLogger();
            composite.addLogger(logger);
        }
        loggerHashMap.put(invocationCls,composite);
    }

    @Override
    public List<InvocationLogger> getLoggers(Class<? extends Invocation> invocation) {

        return loggerHashMap.get(invocation).getLoggers();
    }

    @Override
    public CompositeInvocationLogger getCompositeLogger(Class<? extends Invocation> invocation) {
        return loggerHashMap.get(invocation);
    }
}

package com.easy.logging.logging.selector;

import com.easy.logging.Invocation;
import com.easy.logging.InvocationLogger;
import com.easy.logging.LoggerSelector;
import com.easy.logging.LoggerRegistry;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class StaticTypeLoggerSelector implements LoggerSelector {

    private final LoggerRegistry loggerRegistry;

    protected ConcurrentHashMap<Class<?>,List<InvocationLogger>> hashMap = new ConcurrentHashMap<>();

    public StaticTypeLoggerSelector(LoggerRegistry loggerRegistry){
        this.loggerRegistry = loggerRegistry;
    }
    @Override
    public List<InvocationLogger> select(Invocation invocation) {
        List<InvocationLogger> mapList;
        if((mapList = hashMap.get(invocation.getClass()))!=null){
            return mapList;
        }
        List<? extends InvocationLogger> loggers = loggerRegistry.getLoggers();
        List<InvocationLogger> supportedLoggers = new LinkedList<>();
        for (InvocationLogger logger : loggers) {
            Class[] supportedInvocationType= logger.getSupportedInvocationType();
            Arrays.stream(supportedInvocationType).forEach(supportedType ->{

                if(supportedType.isAssignableFrom(invocation.getClass())){
                    supportedLoggers.add(logger);
                }
            });
        }
        supportedLoggers.sort(Comparator.comparingInt(o -> o.getPriority(invocation.getClass())));
        List<InvocationLogger> highPriority = supportedLoggers.subList(0, 1);
        hashMap.put(invocation.getClass(),highPriority);
        return highPriority;
    }
}

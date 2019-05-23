package com.easy.logging.logging.registry;

import com.easy.logging.Invocation;
import com.easy.logging.InvocationLogger;
import com.easy.logging.LoggerRegistry;

import java.util.LinkedList;
import java.util.List;

public class InvocationLoggerRegistry implements LoggerRegistry {

    protected List<InvocationLogger> loggerList = new LinkedList<>();

    @Override
    public InvocationLogger getLoggerWithHighPriority(Class<? extends Invocation> invocation) {
        InvocationLogger lastLoggerWithHighPriority=null;
        int priority = 0;
        for (int i = 0; i < loggerList.size(); i++) {
            InvocationLogger logger = loggerList.get(i);
            int loggerPriority = logger.getPriority(invocation);
            if(lastLoggerWithHighPriority==null){
                lastLoggerWithHighPriority=logger;
                priority=loggerPriority;
            }else{
                if(priority<loggerPriority){
                    priority = loggerPriority;
                    lastLoggerWithHighPriority=logger;
                }
            }
        }
        return lastLoggerWithHighPriority;
    }

    @Override
    public void registor(InvocationLogger logger) {
        if(!loggerList.contains(logger)){
            loggerList.add(logger);
        }
    }




}

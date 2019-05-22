package com.easy.logging.logging.logger;

import com.easy.logging.Invocation;
import com.easy.logging.InvocationLogger;

import java.util.LinkedList;
import java.util.List;

public class CompositeInvocationLogger implements InvocationLogger {

    protected List<InvocationLogger> loggerList = new LinkedList<>();


    public void addLogger(InvocationLogger logger){

        loggerList.add(logger);

    }

    public List<InvocationLogger> getLoggers(){

        return loggerList;

    }
    @Override
    public void before(Invocation invocation) {
        loggerList.forEach(logger -> logger.before(invocation));
    }

    @Override
    public void after(Invocation invocation, Object result) {
        loggerList.forEach(logger -> logger.after(invocation,result));
    }

    @Override
    public void throwing(Invocation invocation, Throwable throwable) {
        loggerList.forEach(logger -> logger.throwing(invocation,throwable));
    }
}

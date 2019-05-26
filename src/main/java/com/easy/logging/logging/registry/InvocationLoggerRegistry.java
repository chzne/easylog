package com.easy.logging.logging.registry;

import com.easy.logging.Invocation;
import com.easy.logging.InvocationLogger;
import com.easy.logging.LoggerRegistry;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.ObjectProvider;

import javax.annotation.PostConstruct;
import java.util.*;

public class InvocationLoggerRegistry implements LoggerRegistry, InitializingBean {


    private final ObjectProvider<InvocationLogger[]> provider;

    protected List<InvocationLogger> loggerList  = new LinkedList<>();

    public InvocationLoggerRegistry(ObjectProvider<InvocationLogger[]> provider) {
        this.provider = provider;
    }


    @Override
    public void register(InvocationLogger logger) {

        loggerList.add(logger);
    }

    @Override
    public List<? extends InvocationLogger> getLoggersByType(Class<? extends Invocation> clazz) {
        List<InvocationLogger> loggerList = new LinkedList<>();

        for (InvocationLogger invocationLogger : loggerList) {
            
        }

        if(loggerList==null){
            return Collections.emptyList();
        }
        return loggerList;
    }

    @Override
    public List<? extends InvocationLogger> getLoggers() {
        return loggerList;
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        InvocationLogger[] loggers = provider.getIfAvailable();
        if(loggers!=null){
            for (InvocationLogger logger : loggers) {
                register(logger);
            }
        }
    }

}

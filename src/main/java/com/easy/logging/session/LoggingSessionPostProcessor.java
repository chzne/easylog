package com.easy.logging.session;

import com.easy.logging.Invocation;
import com.easy.logging.PostProcessor;
import com.easy.logging.provider.web.http.FilterChainInvocationAdapter;

public class LoggingSessionPostProcessor implements PostProcessor {
    @Override
    public void before(Invocation invocation) {
        if(invocation instanceof FilterChainInvocationAdapter){

        }

    }

    @Override
    public void after(Invocation invocation, Object result) {
        if(invocation instanceof FilterChainInvocationAdapter){

        }
    }

    @Override
    public void throwing(Invocation invocation, Throwable throwable) {

    }
}

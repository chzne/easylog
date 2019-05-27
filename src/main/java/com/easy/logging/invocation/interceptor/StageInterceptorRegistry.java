package com.easy.logging.invocation.interceptor;

import com.easy.logging.InvocationStage;
import com.easy.logging.invocation.InterceptorRegistry;
import com.easy.logging.InvocationInterceptor;

import java.util.HashMap;
import java.util.List;

public class StageInterceptorRegistry implements InterceptorRegistry {

    protected HashMap<InvocationStage,List<InvocationInterceptor>> map = new HashMap<>();
    @Override
    public void register(InvocationInterceptor invocationInterceptor) {

    }

    @Override
    public List<InvocationInterceptor> getInvocationInterceptor(InvocationStage invocationStage) {
        return null;
    }
}

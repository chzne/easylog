package com.easy.logging.invocation;

import com.easy.logging.InvocationInterceptor;
import com.easy.logging.InvocationStage;

import java.util.List;

public interface InterceptorRegistry {


    public void register(InvocationInterceptor invocationInterceptor);

    public List<InvocationInterceptor> getInvocationInterceptor(InvocationStage invocationStage);


}

package com.easy.logging.invocation;

import com.easy.logging.InvocationPostProccessorInterceptor;
import com.easy.logging.InvocationStage;

import java.util.List;

public interface InterceptorRegistry {


    public void register(InvocationStage invocationStage, InvocationPostProccessorInterceptor invocationPostProccessorInterceptor);

    public List<InvocationPostProccessorInterceptor> getInvocationInterceptor(InvocationStage invocationStage);


}

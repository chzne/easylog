package com.easy.logging.provider.web.mvc;

import com.easy.logging.InvocationProxy;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

public class MvcMethodInterceptor implements MethodInterceptor {

    private InvocationProxy invocationProxy;

    public MvcMethodInterceptor(InvocationProxy invocationProxy){
        this.invocationProxy = invocationProxy;
    }

    @Override
    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
        if(invocationProxy !=null){
            return invocationProxy.delegating(new MvcInvocationAdapter(methodInvocation));
        }else{
            return methodInvocation.proceed();
        }

    }


}

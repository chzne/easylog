package com.easy.logging.provider.web;

import com.easy.logging.InvocationDelegator;
import com.easy.logging.provider.web.WebInvocationAdapter;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

public class WebAdvisor implements MethodInterceptor {

    private final InvocationDelegator delegator;

    public WebAdvisor(InvocationDelegator delegator) {
        this.delegator = delegator;
    }

    @Override
    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
        return delegator.invoke(new WebInvocationAdapter(methodInvocation));
    }
}

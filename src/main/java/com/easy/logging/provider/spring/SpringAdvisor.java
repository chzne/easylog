package com.easy.logging.provider.spring;

import com.easy.logging.InvocationDelegator;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

public class SpringAdvisor implements MethodInterceptor {

    private final InvocationDelegator delegator;

    public SpringAdvisor(InvocationDelegator delegator) {
        this.delegator = delegator;
    }

    @Override
    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
        return delegator.invoke(new SpringInvocationAdapter(methodInvocation));
    }
}

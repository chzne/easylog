package com.easy.logging.provider.mybatis;

import com.easy.logging.InvocationProxy;
import com.easy.logging.provider.web.mvc.MvcInvocationAdapter;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

public class MybatisMethodInterceptor implements MethodInterceptor {

    private InvocationProxy invocationProxy;

    public MybatisMethodInterceptor(InvocationProxy invocationProxy) {
        assert invocationProxy!=null;
        this.invocationProxy = invocationProxy;
    }


    @Override
    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
        return invocationProxy.delegating(new MvcInvocationAdapter(methodInvocation));
    }
}

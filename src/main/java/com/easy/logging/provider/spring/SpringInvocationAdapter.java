package com.easy.logging.provider.spring;

import com.easy.logging.invocation.adapter.InvocationAdapter;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.support.AopUtils;

public class SpringInvocationAdapter extends InvocationAdapter {

    private final MethodInvocation methodInvocation;
    private final Class<?> target;

    protected Class<?> getTargetClass(MethodInvocation methodInvocation){
        Class<?> targetClass = methodInvocation.getThis() != null ? AopUtils.getTargetClass(methodInvocation.getThis()) : null;
        return targetClass;
    }

    public SpringInvocationAdapter(MethodInvocation methodInvocation){
        setTarget(methodInvocation.getThis().getClass());
        setMethod(methodInvocation.getMethod());
        setArgurments(methodInvocation.getArguments());
        this.methodInvocation = methodInvocation;
        target = getTargetClass(methodInvocation);

    }
    @Override
    public Object proceed() throws Throwable {
        return methodInvocation.proceed();
    }

    @Override
    public Class<?> getTarget() {
        if(target.getName().contains("proxy")){
            Class<?> cls = getMethod().getDeclaringClass();
            return cls;
        }
        return target;
    }
}

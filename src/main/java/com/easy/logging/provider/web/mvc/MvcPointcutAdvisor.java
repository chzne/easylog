package com.easy.logging.provider.web.mvc;

import com.easy.logging.Advisor;
import org.aopalliance.aop.Advice;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.AbstractPointcutAdvisor;

public class MvcPointcutAdvisor extends AbstractPointcutAdvisor implements Advisor {

    private final MvcAnnotationPointcut pointcut;
    private final MvcMethodInterceptor mvcMethodInterceptor;

    public MvcPointcutAdvisor(MvcAnnotationPointcut pointcut, MvcMethodInterceptor mvcMethodInterceptor){
        this.pointcut = pointcut;
        this.mvcMethodInterceptor = mvcMethodInterceptor;
    }

    @Override
    public Pointcut getPointcut() {
        return pointcut;
    }


    @Override
    public Advice getAdvice() {
        return mvcMethodInterceptor;
    }
}

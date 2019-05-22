package com.easy.logging.provider.spring;

import com.easy.logging.Advisor;
import org.aopalliance.aop.Advice;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.AbstractPointcutAdvisor;

public class SpringProxyPointcutAdvisor extends AbstractPointcutAdvisor implements Advisor {

    private final AnnotationPointcut pointcut;
    private final SpringAdvisor springAdvisor;

    public SpringProxyPointcutAdvisor(AnnotationPointcut pointcut, SpringAdvisor springAdvisor){
        this.pointcut = pointcut;
        this.springAdvisor = springAdvisor;
    }

    @Override
    public Pointcut getPointcut() {

        return pointcut;
    }


    @Override
    public Advice getAdvice() {
        return springAdvisor;
    }
}

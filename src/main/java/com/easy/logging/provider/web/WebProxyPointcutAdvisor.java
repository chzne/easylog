package com.easy.logging.provider.web;

import com.easy.logging.Advisor;
import org.aopalliance.aop.Advice;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.AbstractPointcutAdvisor;

public class WebProxyPointcutAdvisor extends AbstractPointcutAdvisor implements Advisor {

    private final WebAnnotationPointcut pointcut;
    private final WebAdvisor webAdvisor;

    public WebProxyPointcutAdvisor(WebAnnotationPointcut pointcut, WebAdvisor webAdvisor){
        this.pointcut = pointcut;
        this.webAdvisor = webAdvisor;
    }

    @Override
    public Pointcut getPointcut() {

        return pointcut;
    }


    @Override
    public Advice getAdvice() {
        return webAdvisor;
    }
}

package com.easy.logging.provider.mybatis;

import com.easy.logging.Advisor;
import com.easy.logging.provider.web.WebAdvisor;
import com.easy.logging.provider.web.WebAnnotationPointcut;
import org.aopalliance.aop.Advice;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.AbstractPointcutAdvisor;

public class MybatisProxyPointcutAdvisor extends AbstractPointcutAdvisor implements Advisor {

    private final MybatisPackagePointcut pointcut;
    private final MybatisAdvisor webAdvisor;

    public MybatisProxyPointcutAdvisor(MybatisPackagePointcut pointcut, MybatisAdvisor mybatisAdvisor){
        this.pointcut = pointcut;
        this.webAdvisor = mybatisAdvisor;
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

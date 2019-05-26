package com.easy.logging.provider.mybatis;

import com.easy.logging.Advisor;
import org.aopalliance.aop.Advice;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.AbstractPointcutAdvisor;

public class MybatisProxyPointcutAdvisor extends AbstractPointcutAdvisor implements Advisor {

    private final MybatisPackagePointcut pointcut;
    private final MybatisMethodInterceptor webAdvisor;

    public MybatisProxyPointcutAdvisor(MybatisPackagePointcut pointcut, MybatisMethodInterceptor mybatisMethodInterceptor){
        this.pointcut = pointcut;
        this.webAdvisor = mybatisMethodInterceptor;
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

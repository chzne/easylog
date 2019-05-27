package com.easy.logging.provider.web.mvc;

import com.easy.logging.InvocationProxy;
import com.easy.logging.spring.annotation.Logging;
import com.easy.logging.spring.autoconfigure.EasylogAutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;

@Configuration
@ConditionalOnProperty(
        value = {"easylog.log.web.enabled"},
        matchIfMissing = true
)
@ConditionalOnWebApplication
@AutoConfigureAfter(EasylogAutoConfiguration.class)

public class MvcLoggingAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    protected MvcAnnotationPointcut mvcAnnotationPointcut(){
        MvcAnnotationPointcut pointcut = new MvcAnnotationPointcut();
        pointcut.addTargetAnnotation(RestController.class);
        pointcut.addTargetAnnotation(Service.class);
        pointcut.addTargetAnnotation(Repository.class);
        pointcut.addTargetAnnotation(Logging.class);
        pointcut.addMethodAnnotation(Logging.class);
        return pointcut;
    }

    @Bean
    @ConditionalOnMissingBean
    public MvcMethodInterceptor mvcMethodInterceptor(InvocationProxy invocationProxy){
        MvcMethodInterceptor mvcMethodInterceptor = new MvcMethodInterceptor(invocationProxy);
        return mvcMethodInterceptor;
    }


    @Bean
    @ConditionalOnMissingBean
    public MvcPointcutAdvisor mvcPointcutAdvisor(MvcAnnotationPointcut pointcut, MvcMethodInterceptor mvcMethodInterceptor) {
        MvcPointcutAdvisor mvcPointcutAdvisor = new MvcPointcutAdvisor(pointcut, mvcMethodInterceptor);
        return mvcPointcutAdvisor;
    }



}

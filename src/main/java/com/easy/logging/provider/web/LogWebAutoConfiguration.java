package com.easy.logging.provider.web;

import com.easy.logging.InvocationDelegator;
import com.easy.logging.TraceIdGenerator;
import com.easy.logging.TracerRegistry;
import com.easy.logging.provider.dubbo.DubboInvocationAdapter;
import com.easy.logging.provider.dubbo.DubboTracer;
import com.easy.logging.spring.annotation.Logging;
import com.easy.logging.spring.autoconfigure.EasylogAutoConfiguration;
import com.easy.logging.trace.registry.SimpleTraceRegistry;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.Map;

@Configuration
@ConditionalOnProperty(
        value = {"easylog.log.web.enabled"},
        matchIfMissing = true
)
@ConditionalOnWebApplication
@AutoConfigureAfter({EasylogAutoConfiguration.class})
public class LogWebAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public ServletAdvisor servletAdvisor(InvocationDelegator invocationDelegator) {
        ServletAdvisor logFilter =   new ServletAdvisor(invocationDelegator);
        logFilter.setIncludePayload(false);
        logFilter.setMaxPayloadLength(10000);
        return logFilter;
    }

    @Bean
    @ConditionalOnMissingBean
    public ServletInvocationLogger servletInvocationLogger() {
        ServletInvocationLogger logger = new ServletInvocationLogger();
        logger.setIncludeQueryString(true);
        logger.setIncludePayload(false);
        logger.setIncludeHeaders(false);
        logger.setIncludeClientInfo(false);
        logger.setMaxPayloadLength(10000);
        logger.setBeforeMessagePrefix("HTTP 开始: ");
        logger.setAfterMessagePrefix("HTTP 结束: ");
        logger.setElaspedTimeName(" 耗时:");
        return logger;
    }

    @Bean
    @ConditionalOnMissingBean
    public ServletTracer servletTracer(TraceIdGenerator traceIdGenerator){
        ServletTracer servletTracer = new ServletTracer();
        servletTracer.setTraceParameterName("traceId");
        servletTracer.setIdGenerator(traceIdGenerator);
        return servletTracer;
    }


    @Bean
    @ConditionalOnMissingBean
    protected WebAnnotationPointcut webAnnotationPointcut(){
        WebAnnotationPointcut pointcut = new WebAnnotationPointcut();
        pointcut.addTargetAnnotation(RestController.class);
        pointcut.addTargetAnnotation(Service.class);
        pointcut.addTargetAnnotation(Repository.class);
        pointcut.addTargetAnnotation(Logging.class);
        pointcut.addMethodAnnotation(Logging.class);
        return pointcut;
    }


    @Bean
    @ConditionalOnMissingBean
    public WebProxyPointcutAdvisor webProxyPointcutAdvisor(InvocationDelegator invocationDelegator,WebAnnotationPointcut pointcut) {
        WebAdvisor advice = new WebAdvisor(invocationDelegator);
        WebProxyPointcutAdvisor webProxyPointcutAdvisor = new WebProxyPointcutAdvisor(pointcut,advice);
        return webProxyPointcutAdvisor;
    }




}

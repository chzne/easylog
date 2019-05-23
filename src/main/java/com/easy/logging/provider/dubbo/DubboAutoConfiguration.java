package com.easy.logging.provider.dubbo;

import com.easy.logging.InvocationDelegator;
import com.easy.logging.TraceIdGenerator;
import com.easy.logging.TracerRegistry;
import com.easy.logging.provider.web.ServletAdvisor;
import com.easy.logging.provider.web.ServletInvocationAdapter;
import com.easy.logging.provider.web.ServletInvocationLogger;
import com.easy.logging.provider.web.ServletTracer;
import com.easy.logging.spring.autoconfigure.EasylogAutoConfiguration;
import com.easy.logging.trace.registry.SimpleTraceRegistry;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(
        value = {"easylog.log.dubbo.enabled"},
        matchIfMissing = true
)
@ConditionalOnWebApplication
@AutoConfigureAfter({EasylogAutoConfiguration.class})
public class DubboAutoConfiguration {

    private final InvocationDelegator invocationDelegator;
    private final TraceIdGenerator traceIdGenerator;


    public DubboAutoConfiguration(TraceIdGenerator traceIdGenerator, InvocationDelegator invocationDelegator){
        this.traceIdGenerator = traceIdGenerator;
        this.invocationDelegator = invocationDelegator;

    }

    @Bean
    @ConditionalOnMissingBean
    public DubboTracer dubboTracer(){
        DubboTracer dubboTracer = new DubboTracer();
        dubboTracer.setIdGenerator(traceIdGenerator);
        return dubboTracer;
    }




}

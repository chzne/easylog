package com.easy.logging.provider.dubbo;

import com.easy.logging.InvocationProxy;
import com.easy.logging.logging.config.InvocationLoggingConfig;
import com.easy.logging.spring.autoconfigure.EasylogAutoConfiguration;
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
public class DubboLoggingAutoConfiguration {

    private final InvocationProxy invocationProxy;

    public DubboLoggingAutoConfiguration(InvocationProxy invocationProxy){

        this.invocationProxy = invocationProxy;
    }

    @Bean
    @ConditionalOnMissingBean
    public DubboLoggingTracer dubboTracer(){
        DubboLoggingTracer dubboLoggingTracer = new DubboLoggingTracer();
        return dubboLoggingTracer;
    }

    @Bean
    @ConditionalOnMissingBean
    public DubboInvocationLogger dubboInvocationLogger(InvocationLoggingConfig commonInvocationLoggingConfig){
        DubboInvocationLogger logger = new DubboInvocationLogger(commonInvocationLoggingConfig);
        return logger;
    }




}

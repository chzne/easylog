package com.easy.logging.provider.web.http;

import com.easy.logging.spring.autoconfigure.EasylogAutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

@Configuration
@ConditionalOnWebApplication
@AutoConfigureAfter(EasylogAutoConfiguration.class)
public class HttpLoggingAutoConfiguration {




    @Bean
    @ConditionalOnMissingBean
    public HttpLoggingTracer httpLoggingTracer(){
        HttpLoggingTracer loggingTracer = new HttpLoggingTracer();
        loggingTracer.setTraceParameterName("traceId");
        return loggingTracer;
    }

    @Bean
    @ConditionalOnMissingBean
    public HttpInvocationLogger httpInvocationLogger(){
        HttpInvocationLogger logger = new HttpInvocationLogger();
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
    public RequestMappingFilterProxy requestMappingFilterProxy(){
        HttpLoggingFilter loggingFilter = new HttpLoggingFilter();
        RequestMappingFilterProxy requestMappingFilterProxy = new RequestMappingFilterProxy(loggingFilter);
        return requestMappingFilterProxy;
    }
}

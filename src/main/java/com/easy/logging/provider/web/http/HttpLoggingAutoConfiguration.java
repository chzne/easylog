package com.easy.logging.provider.web.http;

import com.easy.logging.LoggingPayload;
import com.easy.logging.SessionListener;
import com.easy.logging.logging.config.InvocationLoggingConfig;
import com.easy.logging.spring.autoconfigure.EasylogAutoConfiguration;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.ObjectProvider;
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
    public HttpInvocationLogger httpInvocationLogger(InvocationLoggingConfig commonInvocationLoggingConfig, ObjectProvider<LoggingPayload[]> loggingPayloadProviders){
        InvocationLoggingConfig config = new InvocationLoggingConfig();
        BeanUtils.copyProperties(commonInvocationLoggingConfig,config);
        config.setBeforePrefix("【请求地址】");
        config.setAfterPrefix("【请求结束】");
        HttpInvocationLogger logger = new HttpInvocationLogger(config,loggingPayloadProviders);
        logger.setIncludeQueryString(true);
        logger.setIncludePayload(true);
        logger.setIncludeHeaders(false);
        logger.setIncludeClientInfo(false);
        logger.setMaxPayloadLength(10000);

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

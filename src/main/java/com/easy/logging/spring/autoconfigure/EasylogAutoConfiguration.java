package com.easy.logging.spring.autoconfigure;

import com.easy.logging.*;
import com.easy.logging.invocation.InterceptorRegistry;
import com.easy.logging.InvocationInterceptor;
import com.easy.logging.InvocationProxy;
import com.easy.logging.PostProcessor;
import com.easy.logging.invocation.advice.OrderedCompositePostProcessor;
import com.easy.logging.invocation.interceptor.exception.PropagatingExceptionInterceptor;
import com.easy.logging.invocation.proxy.LogInvocationProxy;
import com.easy.logging.invocation.interceptor.StageInterceptorRegistry;
import com.easy.logging.logging.config.InvocationLoggingConfig;
import com.easy.logging.logging.logger.GenericInvocationLogger;
import com.easy.logging.logging.processor.LoggingPostProcessor;
import com.easy.logging.logging.registry.InvocationLoggerRegistry;
import com.easy.logging.logging.selector.StaticTypeLoggerSelector;

import com.easy.logging.session.DistributedSessionFactory;
import com.easy.logging.trace.registry.TypeTracerRegistrar;
import com.easy.logging.trace.advice.TracePostProcessor;
import com.easy.logging.trace.generator.IncrementTraceIdGenerator;
import com.easy.logging.trace.registry.SimpleTracerRegistry;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;


@EnableConfigurationProperties(EasylogProperties.class)
@Configuration
@ConditionalOnWebApplication
public class EasylogAutoConfiguration  implements ApplicationContextAware {



    private ApplicationContext applicationContext;
    @Bean
    @ConditionalOnMissingBean
    public TraceIdGenerator traceIdGenerator() {
        return new IncrementTraceIdGenerator();
    }


    @Bean
    @ConditionalOnMissingBean
    public InvocationLoggingConfig commonInvocationLoggingConfig(){
        InvocationLoggingConfig config = new InvocationLoggingConfig();
        config.setIncludeArgurments(true);
        config.setIncludeResult(true);
        config.setIncludeException(true);
        config.setIncludeElapsedTime(true);
        config.setBeforePrefix("【调用方法】");
        config.setAfterPrefix("【返回结果】");
        config.setElapsedTimePrefix(" 【耗时:");
        config.setElapsedTimeSuffix("】");
        config.setExceptionPrefix("【发生异常】");
        config.setExceptionClassPrefix("【异常类】");
        config.setExceptionMessagePrefix("【异常消息】");
        config.setExceptionLocationPrefix("【异常位置】");
        return config;
    }
    @Bean
    @ConditionalOnMissingBean
    public GenericInvocationLogger genericInvocationLogger(InvocationLoggingConfig commonInvocationLoggingConfig){
        GenericInvocationLogger logger = new GenericInvocationLogger(commonInvocationLoggingConfig);

        return logger;
    }

    @Bean
    @ConditionalOnMissingBean
    public LoggerRegistry loggerRegistry(ObjectProvider<InvocationLogger[]> provider) {
        InvocationLoggerRegistry registry = new InvocationLoggerRegistry(provider);
        return registry;
    }



    @Bean
    @ConditionalOnMissingBean
    public TracerRegistry tracerRegistry() {
        TracerRegistry registry =  new SimpleTracerRegistry();
        return registry;
    }

    @Bean
    @ConditionalOnMissingBean
    public TracerRegistrar tracerRegistrar(TracerRegistry tracerRegistry, ObjectProvider<Tracer[]> provider){
        TracerRegistrar tracerRegistrar = new TypeTracerRegistrar();
        Tracer[] tracers;
        if((tracers = provider.getIfAvailable())!=null){
            provider.getIfAvailable();
            Arrays.stream(tracers).forEach(tracer -> tracerRegistrar.addTracer(tracerRegistry,tracer));
        }
        return tracerRegistrar;
    }

    @Bean
    @ConditionalOnMissingBean
    public LoggingPostProcessor loggingPostProcessor(ObjectProvider<LoggerSelector> objectProvider) {
        return new LoggingPostProcessor(objectProvider.getIfAvailable());
    }




    @Bean
    @ConditionalOnMissingBean
    public StaticTypeLoggerSelector staticTypeLoggerSelector(LoggerRegistry registry){

        StaticTypeLoggerSelector selector = new StaticTypeLoggerSelector(registry);
        return selector;
    }

    @Bean
    @ConditionalOnMissingBean
    public SessionFactory sessionFactory(){
        DistributedSessionFactory sessionFactory = new DistributedSessionFactory();
        return sessionFactory;

    }
    @Bean
    @ConditionalOnMissingBean
    public TracePostProcessor tracePostProcessor(TracerRegistry tracerRegistry,TraceIdGenerator traceIdGenerator){
        TracePostProcessor postProcessor = new TracePostProcessor(tracerRegistry,traceIdGenerator);
        return postProcessor;
    }


    @Bean
    @ConditionalOnMissingBean
    public PropagatingExceptionInterceptor propagatingExceptionInterceptor(){
        PropagatingExceptionInterceptor interceptor = new PropagatingExceptionInterceptor();
        return interceptor;
    }

    @Bean
    @ConditionalOnMissingBean
    public InterceptorRegistry interceptorRegistry(ObjectProvider<InvocationInterceptor[]> provider){
        InterceptorRegistry interceptorRegistry = new StageInterceptorRegistry();
        InvocationInterceptor[] invocationIntercepts = provider.getIfAvailable();
        if(invocationIntercepts==null){
            return interceptorRegistry;
        }
        Arrays.stream(invocationIntercepts).forEach(ii ->interceptorRegistry.register(ii));
        return interceptorRegistry;
    }



    @Bean
    @ConditionalOnMissingBean
    public InvocationProxy invocationProxy(SessionFactory sessionFactory,ObjectProvider<PostProcessor[]> provider,
                                           InterceptorRegistry interceptorRegistry) {

        OrderedCompositePostProcessor postProcessor = new OrderedCompositePostProcessor(provider);

        LogInvocationProxy proxy = new LogInvocationProxy(sessionFactory,postProcessor,interceptorRegistry);
        return proxy;
    }



    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }




}

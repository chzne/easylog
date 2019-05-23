package com.easy.logging.spring.autoconfigure;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.PatternLayout;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import ch.qos.logback.core.OutputStreamAppender;
import ch.qos.logback.core.encoder.LayoutWrappingEncoder;
import com.easy.logging.*;
import com.easy.logging.invocation.delegator.DefaultInvocationDelegator;
import com.easy.logging.logging.advice.LoggingAdvice;
import com.easy.logging.logging.logger.GenericInvocationLogger;
import com.easy.logging.logging.registry.InvocationLoggerRegistry;
import com.easy.logging.provider.logback.TraceEnsureExceptionHandling;
import com.easy.logging.spring.annotation.EasyLogWebMvcConfiguration;
import com.easy.logging.trace.Trace;
import com.easy.logging.trace.advice.TraceAdvice;
import com.easy.logging.trace.generator.IncrementTraceIdGenerator;
import com.easy.logging.trace.registry.SimpleTraceRegistry;
import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.ILoggerFactory;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.Arrays;
import java.util.Iterator;


@EnableConfigurationProperties(EasylogProperties.class)
@Configuration
@ConditionalOnBean(EasyLogWebMvcConfiguration.class)
public class EasylogAutoConfiguration  implements ApplicationContextAware {



    private ApplicationContext applicationContext;

    public EasylogAutoConfiguration(ApplicationContext applicationContext,SqlSessionFactory sqlSessionFactory){
        this.applicationContext=applicationContext;
        logbackTraceInjection();
    }


    public void logbackTraceInjection(){
        ILoggerFactory iLoggerFactory = LoggerFactory.getILoggerFactory();
        if (iLoggerFactory instanceof LoggerContext) {
            LoggerContext loggerContext = (LoggerContext) iLoggerFactory;
            Logger logger = loggerContext.getLogger("ROOT");
            Iterator<Appender<ILoggingEvent>> appenders = logger.iteratorForAppenders();
            while (appenders.hasNext()) {
                Appender<ILoggingEvent> appender =  appenders.next();
                if(OutputStreamAppender.class.isAssignableFrom(appender.getClass())){
                    LayoutWrappingEncoder encoder = (LayoutWrappingEncoder) ((OutputStreamAppender<ILoggingEvent>) appender).getEncoder();
                    PatternLayout layout = (PatternLayout) encoder.getLayout();
                    String pattern = layout.getPattern();
                    layout.setPattern("%X{"+ Trace.DEFAULT_TRACE_MDC_PARAMETER_NAME+"}"+pattern);
                    layout.stop();
                    layout.setPostCompileProcessor(new TraceEnsureExceptionHandling());
                    layout.start();
                }
            }
        }
    }

    @Bean
    @ConditionalOnMissingBean
    public TraceIdGenerator traceIdGenerator() {
        return new IncrementTraceIdGenerator();
    }

    @Bean
    @ConditionalOnMissingBean
    public GenericInvocationLogger genericInvocationLogger(){
        GenericInvocationLogger genericInvocationLogger = new GenericInvocationLogger();
        genericInvocationLogger.setBeforePrefix("开始:");
        genericInvocationLogger.setAfterPrefix("结束:");
        genericInvocationLogger.setElaspedTimeName(" 耗时:");
        return genericInvocationLogger;
    }

    @Bean
    @ConditionalOnMissingBean
    public LoggerRegistry loggerRegistry(ObjectProvider<InvocationLogger[]> invocationLoggers) {
        InvocationLoggerRegistry registry = new InvocationLoggerRegistry();
        InvocationLogger[] loggers = invocationLoggers.getIfAvailable();
        Arrays.stream(loggers).forEach(logger ->{
            registry.registor(logger);
        });
        return registry;
    }

    @Bean
    @ConditionalOnMissingBean
    public TracerRegistry tracerRegistry(ObjectProvider<Tracer[]> objectProviderTracers) {
        TracerRegistry registry =  new SimpleTraceRegistry();
        Tracer[] tracers = objectProviderTracers.getIfAvailable();
        Arrays.stream(tracers).forEach(tracer ->{
            Class<? extends Tracer> clazz = tracer.getClass();
            Type[] types = clazz.getGenericInterfaces();
            Type type = types[0];
            if(ParameterizedType.class.isAssignableFrom(type.getClass())){
                ParameterizedType parameterizedType = (ParameterizedType) type;
                Type actualType = parameterizedType.getActualTypeArguments()[0];
                if(TypeVariable.class.isAssignableFrom(actualType.getClass())){
                    TypeVariable typeVariable = (TypeVariable) actualType;
                    Class<? extends Invocation> invocationType = (Class<? extends Invocation>) typeVariable.getBounds()[0];
                    registry.registor(invocationType,tracer);
                }else{
                    Class<? extends Invocation> invocationType = (Class<? extends Invocation>) actualType;
                    registry.registor(invocationType,tracer);
                }
            }

        });
        return registry;
    }

    @Bean
    @ConditionalOnMissingBean
    public LoggingAdvice loggingAdvice(@Autowired LoggerRegistry loggerRegistry) {
        return new LoggingAdvice(loggerRegistry);
    }

    @Bean
    @ConditionalOnMissingBean
    public TraceAdvice traceAdvice(@Autowired TracerRegistry tracerRegistry) {
        TraceAdvice traceAdvice = new TraceAdvice(tracerRegistry);
        return traceAdvice;
    }

    @Bean
    @ConditionalOnMissingBean
    public InvocationDelegator invocationDelegator(LoggingAdvice loggingAdvice,TraceAdvice traceAdvice) {
        DefaultInvocationDelegator delegator = new DefaultInvocationDelegator(new Advice[]{loggingAdvice,traceAdvice});
        return delegator;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }


}

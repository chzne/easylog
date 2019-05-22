package com.easy.logging.spring.autoconfigure;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.PatternLayout;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import ch.qos.logback.core.OutputStreamAppender;
import ch.qos.logback.core.encoder.LayoutWrappingEncoder;

import com.easy.logging.*;
import com.easy.logging.logging.advice.LoggingAdvice;
import com.easy.logging.logging.logger.GenericInvocationLogger;
import com.easy.logging.logging.registry.InvocationLoggerRegistry;
import com.easy.logging.provider.servlet.ServletAdvisor;
import com.easy.logging.provider.servlet.ServletInvocationLogger;

import com.easy.logging.invocation.delegator.AdviceInvocationDelegator;
import com.easy.logging.provider.dubbo.DubboInvocationAdapter;
import com.easy.logging.provider.dubbo.DubboTracer;
import com.easy.logging.provider.logback.TraceEnsureExceptionHandling;
import com.easy.logging.provider.mybatis.MybatisLogInterceptor;
import com.easy.logging.provider.servlet.ServletInvocationAdapter;
import com.easy.logging.provider.servlet.ServletTracer;
import com.easy.logging.provider.spring.AnnotationPointcut;
import com.easy.logging.provider.spring.SpringAdvisor;
import com.easy.logging.provider.spring.SpringInvocationAdapter;
import com.easy.logging.provider.spring.SpringProxyPointcutAdvisor;
import com.easy.logging.spring.annotation.EasyLogWebMvcConfiguration;
import com.easy.logging.spring.annotation.Logging;
import com.easy.logging.trace.Trace;
import com.easy.logging.trace.advice.TraceAdvice;
import com.easy.logging.trace.generator.IncrementTraceIdGenerator;
import com.easy.logging.trace.registry.SimpleTraceRegistry;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.ILoggerFactory;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;


@EnableConfigurationProperties(EasylogProperties.class)
@Configuration
@ConditionalOnBean(EasyLogWebMvcConfiguration.class)
public class EasylogAutoConfiguration  implements ApplicationContextAware {


    private ApplicationContext applicationContext;

    public EasylogAutoConfiguration(ApplicationContext applicationContext,SqlSessionFactory sqlSessionFactory){
        this.applicationContext=applicationContext;
        MybatisLogInterceptor interceptor = new MybatisLogInterceptor();
        sqlSessionFactory.getConfiguration().addInterceptor(interceptor);
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
    public InvocationDelegator invocationDelegator(@Autowired ObjectProvider<Advice[]> advices) {
        AdviceInvocationDelegator delegator = new AdviceInvocationDelegator(advices);
        return delegator;
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
    public TraceIdGenerator traceIdGenerator() {
        return new IncrementTraceIdGenerator();
    }

    @Bean
    @ConditionalOnMissingBean
    public TracerRegistry tracerRegistry(@Autowired TraceIdGenerator traceIdGenerator) {
        TracerRegistry registry =  new SimpleTraceRegistry();
        ServletTracer servletTracer = new ServletTracer();
        DubboTracer dubboTracer = new DubboTracer();
        servletTracer.setTraceParameterName("traceId");
        servletTracer.setIdGenerator(traceIdGenerator);
        registry.registor(ServletInvocationAdapter.class,servletTracer);
        registry.registor(DubboInvocationAdapter.class,dubboTracer);
        return registry;
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
    public LoggerRegistry loggerRegistry(@Autowired ServletInvocationLogger servletInvocationLogger, @Autowired GenericInvocationLogger genericInvocationLogger) {

        InvocationLoggerRegistry registry = new InvocationLoggerRegistry();
        registry.registor(ServletInvocationAdapter.class,servletInvocationLogger);
        registry.registor(SpringInvocationAdapter.class, genericInvocationLogger);
        registry.registor(DubboInvocationAdapter.class,genericInvocationLogger);
        return registry;
    }

    @Bean
    @ConditionalOnMissingBean
    public SpringProxyPointcutAdvisor loggingAdvisor(@Autowired InvocationDelegator invocationDelegator,@Autowired AnnotationPointcut pointcut) {

        SpringAdvisor advice = new SpringAdvisor(invocationDelegator);
        SpringProxyPointcutAdvisor springProxyPointcutAdvisor = new SpringProxyPointcutAdvisor(pointcut,advice);
        return springProxyPointcutAdvisor;
    }

    @Bean
    @ConditionalOnClass({SqlSessionFactory.class, SqlSessionFactoryBean.class})
    public AnnotationPointcut annotationPointcutWithMybatis(){
        AnnotationPointcut pointcut = basePointcut();
        Map<String, Object> beansWithAnnotationMap = applicationContext.getBeansWithAnnotation(MapperScan.class);
        for(Map.Entry<String, Object> entry : beansWithAnnotationMap.entrySet()){
            Class<?> clazz = entry.getValue().getClass();
            MapperScan mapperScan = AnnotationUtils.findAnnotation(clazz, MapperScan.class);
            if(mapperScan!=null){
                Arrays.stream(mapperScan.basePackages()).forEach(s -> pointcut.addPackageName(s));
                Arrays.stream(mapperScan.basePackageClasses()).forEach(ac ->pointcut.addPackageName(ac.getPackage().getName()));

            }

        }

        return pointcut;

    }

    protected AnnotationPointcut basePointcut(){
        AnnotationPointcut pointcut = new AnnotationPointcut();
        pointcut.addTargetAnnotation(RestController.class);
        pointcut.addTargetAnnotation(Service.class);
        pointcut.addTargetAnnotation(Repository.class);
        pointcut.addTargetAnnotation(Logging.class);
        pointcut.addMethodAnnotation(Logging.class);
        return pointcut;
    }
    @Bean
    @ConditionalOnMissingBean(AnnotationPointcut.class)
    public AnnotationPointcut annotationPointcut(){

        return basePointcut();
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
    public ServletAdvisor servletLogFilter(@Autowired InvocationDelegator invocationDelegator) {
        ServletAdvisor logFilter =   new ServletAdvisor(invocationDelegator);
        logFilter.setIncludePayload(false);
        logFilter.setMaxPayloadLength(10000);
        return logFilter;
    }


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}

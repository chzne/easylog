package com.easy.logging.provider.mybatis;

import com.easy.logging.InvocationDelegator;
import com.easy.logging.provider.web.WebAdvisor;
import com.easy.logging.provider.web.WebAnnotationPointcut;
import com.easy.logging.provider.web.WebProxyPointcutAdvisor;
import com.easy.logging.spring.annotation.Logging;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.BeansException;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.Map;

@Configuration
@ConditionalOnWebApplication
 public class LogMybatisAutoconfiguration implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Bean
    @ConditionalOnMissingBean
    public MybatisLogInterceptor mybatisLogInterceptor() {
        MybatisLogInterceptor mybatisLogInterceptor =   new MybatisLogInterceptor();
        return mybatisLogInterceptor;
    }

    @Bean
    @ConditionalOnMissingBean
    protected MybatisPackagePointcut mybatisPackagePointcut(){
        MybatisPackagePointcut mybatisPackagePointcut = new MybatisPackagePointcut();
        Map<String, Object> beansWithAnnotationMap = applicationContext.getBeansWithAnnotation(MapperScan.class);
        for(Map.Entry<String, Object> entry : beansWithAnnotationMap.entrySet()){
            Class<?> clazz = entry.getValue().getClass();
            MapperScan mapperScan = AnnotationUtils.findAnnotation(clazz, MapperScan.class);
            if(mapperScan!=null){
                Arrays.stream(mapperScan.basePackages()).forEach(s -> mybatisPackagePointcut.addPackageName(s));
                Arrays.stream(mapperScan.basePackageClasses()).forEach(ac ->mybatisPackagePointcut.addPackageName(ac.getPackage().getName()));
            }
        }
        return mybatisPackagePointcut;
    }



    @Bean
    @ConditionalOnMissingBean
    public MybatisProxyPointcutAdvisor mybatisProxyPointcutAdvisor(InvocationDelegator invocationDelegator, MybatisPackagePointcut pointcut) {
        MybatisAdvisor advice = new MybatisAdvisor(invocationDelegator);
        MybatisProxyPointcutAdvisor mybatisProxyPointcutAdvisor = new MybatisProxyPointcutAdvisor(pointcut,advice);
        return mybatisProxyPointcutAdvisor;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}

package com.easy.logging.provider.mybatis;

import com.easy.logging.InvocationProxy;
import com.easy.logging.spring.autoconfigure.EasylogAutoConfiguration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.BeansException;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.AnnotationUtils;

import java.util.Arrays;
import java.util.Map;

@Configuration
@ConditionalOnProperty(
        value = {"easylog.log.mybatis.enabled"},
        matchIfMissing = true
)
@ConditionalOnWebApplication
@ConditionalOnClass(SqlSessionFactory.class)
@AutoConfigureAfter({EasylogAutoConfiguration.class})
 public class MybatisLoggingAutoConfiguration implements ApplicationContextAware {

    private ApplicationContext applicationContext;



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
    public MybatisMethodInterceptor mybatisMethodInterceptor(InvocationProxy invocationProxy){
        return new MybatisMethodInterceptor(invocationProxy);
    }



    @Bean
    @ConditionalOnMissingBean
    public MybatisProxyPointcutAdvisor mybatisProxyPointcutAdvisor(MybatisPackagePointcut pointcut,MybatisMethodInterceptor mybatisMethodInterceptor) {

        MybatisProxyPointcutAdvisor mybatisProxyPointcutAdvisor = new MybatisProxyPointcutAdvisor(pointcut,mybatisMethodInterceptor);
        return mybatisProxyPointcutAdvisor;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}

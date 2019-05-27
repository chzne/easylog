package com.easy.logging.spring;

import com.easy.logging.spring.annotation.EasylogBeanPostProcessor;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EasylogConfiguration {

    @Bean
    public BeanPostProcessor easylogBeanPostProcessor(){
        EasylogBeanPostProcessor postProcessor = new EasylogBeanPostProcessor();
        return postProcessor;
    }


}

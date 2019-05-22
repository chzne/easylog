package com.easy.logging.spring.annotation;

import com.easy.logging.provider.servlet.ServletAdvisor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

@Configuration
public class EasyLogWebMvcConfiguration extends WebMvcConfigurationSupport {

    @Autowired
    protected ServletAdvisor servletAdvisor;






    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(servletAdvisor);
    }
}

package com.easy.logging.spring.annotation;

import com.easy.logging.SessionListener;
import com.easy.logging.InvocationProxy;
import com.easy.logging.InvocationProxyHolder;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.Ordered;

public class EasylogBeanPostProcessor implements BeanPostProcessor, ApplicationContextAware , Ordered {


    private ApplicationContext applicationContext;

    @Override
    public Object postProcessBeforeInitialization(Object bean, String s) throws BeansException {
        if (bean instanceof InvocationProxyAware) {
            InvocationProxy invocationProxy = applicationContext.getBean(InvocationProxy.class);
            ((InvocationProxyAware) bean).setInvocationProxy(invocationProxy);
        }

        if(bean instanceof SessionListener){
            InvocationProxy invocationProxy = applicationContext.getBean(InvocationProxy.class);
            invocationProxy.addSessionListener((SessionListener) bean);
        }
        if(InvocationProxyHolder.getInvocationProxy()==null){
            InvocationProxy invocationProxy = applicationContext.getBean(InvocationProxy.class);
            InvocationProxyHolder.setInvocationProxy(invocationProxy);
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public int getOrder() {
        return -1000;
    }
}

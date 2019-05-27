package com.easy.logging.provider.web.http;

import com.easy.logging.Advisor;
import com.easy.logging.InvocationProxy;
import com.easy.logging.spring.annotation.InvocationProxyAware;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.GenericFilterBean;


import javax.servlet.FilterChain;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

@Slf4j
public class HttpLoggingFilter extends GenericFilterBean implements Advisor, InvocationProxyAware {

    private InvocationProxy invocationProxy;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) {
        Class<? extends HttpLoggingFilter> target = this.getClass();
        Class[] args = new Class[]{ServletRequest.class,ServletResponse.class,FilterChain.class};
        try {

            Method method = target.getMethod("doFilter", args);
            FilterChainInvocationAdapter filterChainInvocationAdapter = new FilterChainInvocationAdapter(target,method,(HttpServletRequest)servletRequest,(HttpServletResponse)servletResponse,filterChain );
            invocationProxy.delegating(filterChainInvocationAdapter);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }



    }

    @Override
    public void setInvocationProxy(InvocationProxy invocationProxy) {
        this.invocationProxy = invocationProxy;
    }
}

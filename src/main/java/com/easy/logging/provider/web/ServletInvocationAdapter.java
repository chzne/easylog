package com.easy.logging.provider.web;

import com.easy.logging.invocation.adapter.InvocationAdapter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ServletInvocationAdapter extends InvocationAdapter {


    private final FilterChain filterChain;
    private final HttpServletRequest request;
    private final HttpServletResponse response;

    public ServletInvocationAdapter(HttpServletRequest request,HttpServletResponse response,FilterChain filterChain) {
        setTarget(ServletAdvisor.class);
        this.filterChain = filterChain;
        this.request= request;
        this.response= response;
    }

    public HttpServletRequest getRequest(){
        return request;
    }

    @Override
    public Object proceed() throws Throwable {

         filterChain.doFilter(request,response);
         return Void.class;
    }
}

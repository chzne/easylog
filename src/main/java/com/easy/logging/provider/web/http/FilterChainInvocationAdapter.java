package com.easy.logging.provider.web.http;

import com.easy.logging.invocation.adapter.InvocationAdapter;

import javax.servlet.FilterChain;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

public class FilterChainInvocationAdapter extends InvocationAdapter {

    protected HttpServletRequest httpServletRequest;

    public HttpServletResponse getHttpServletResponse() {
        return httpServletResponse;
    }

    protected HttpServletResponse httpServletResponse;

    protected FilterChain filterChain;

    public FilterChainInvocationAdapter(Class target, Method method, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) {
        setTarget(target);
        setMethod(method);
        this.httpServletRequest = httpServletRequest;
        this.httpServletResponse = httpServletResponse;
        this.filterChain = filterChain;
    }





    public HttpServletRequest getHttpServletRequest() {
        return httpServletRequest;
    }



    public FilterChain getFilterChain() {
        return filterChain;
    }



    @Override
    public Object doInvoke() throws Throwable {
         filterChain.doFilter(httpServletRequest,httpServletResponse);
         return Void.class;
    }
}

package com.easy.logging.provider.web.http;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.easy.logging.Tracer;
import com.easy.logging.TraceAttachment;

public class HttpLoggingTracer implements Tracer<FilterChainInvocationAdapter> {


    public final static String SERVLET_TRACE_ID_KEY = "traceId";

    protected String traceParameterName=SERVLET_TRACE_ID_KEY;



    @Override
    public boolean canExtract(FilterChainInvocationAdapter invocation) {
        return true;
    }

    public void setTraceParameterName(String traceParameterName){
        this.traceParameterName=traceParameterName;
    }

    @Override
    public TraceAttachment extract(FilterChainInvocationAdapter invocation) {
        String traceId;
        String[] values = invocation.getHttpServletRequest().getParameterValues(traceParameterName);
        if (values != null && values.length > 0) {
            traceId = values[0];
        } else {

            traceId = invocation.getHttpServletRequest().getHeader(traceParameterName);
        }
        if (null == traceId || StringUtils.isBlank(traceId)) {
            return null;
        }
        TraceAttachment traceAttachment = new TraceAttachment(traceId);
        return traceAttachment;
    }

    @Override
    public boolean canInject(FilterChainInvocationAdapter invocation) {
        return false;
    }

    @Override
    public void inject(FilterChainInvocationAdapter invocation, TraceAttachment traceAttachment) {

    }
}

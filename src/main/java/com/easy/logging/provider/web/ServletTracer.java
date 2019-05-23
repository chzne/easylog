package com.easy.logging.provider.web;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.easy.logging.Tracer;
import com.easy.logging.trace.Attachment;
import com.easy.logging.TraceIdGenerator;

public class ServletTracer implements Tracer<ServletInvocationAdapter> {

    private TraceIdGenerator traceIdGenerator;

    public final static String SERVLET_TRACE_ID_KEY = "traceId";

    protected String traceParameterName=SERVLET_TRACE_ID_KEY;

    @Override
    public void setIdGenerator(TraceIdGenerator traceIdGenerator) {

        this.traceIdGenerator= traceIdGenerator;

    }

    @Override
    public TraceIdGenerator getIdGenerator() {
        return traceIdGenerator;
    }

    @Override
    public boolean canExtract(ServletInvocationAdapter invocation) {
        return true;
    }

    public void setTraceParameterName(String traceParameterName){
        this.traceParameterName=traceParameterName;
    }

    @Override
    public Attachment extract(ServletInvocationAdapter invocation) {
        String traceId;
        String[] values = invocation.getRequest().getParameterValues(traceParameterName);
        if (values != null && values.length > 0) {
            traceId = values[0];
        } else {

            traceId = invocation.getRequest().getHeader(traceParameterName);
        }
        if (null == traceId || StringUtils.isBlank(traceId)) {
            return null;
        }
        Attachment attachment = new Attachment(traceId);
        return attachment;
    }

    @Override
    public boolean canInject(ServletInvocationAdapter invocation) {
        return false;
    }

    @Override
    public void inject(ServletInvocationAdapter invocation, Attachment attachment) {

    }
}

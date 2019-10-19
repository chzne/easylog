package com.easy.logging;

import org.slf4j.MDC;
import org.springframework.util.StringUtils;

public class Trace {

    protected static ThreadLocal<Trace> traceThreadLocal = new ThreadLocal<>();

    public final static String DEFAULT_TRACE_MDC_PARAMETER_NAME = "traceId";

    private final String traceId;

    protected Trace(String traceId) {

        this.traceId = traceId;
        traceThreadLocal.set(this);
        MDC.put(DEFAULT_TRACE_MDC_PARAMETER_NAME, "["+traceId+"]");

    }

    public static class TraceBuilder{

        public static Trace build(Tracer tracer, Invocation invocation,TraceIdGenerator traceIdGenerator){
            TraceAttachment traceAttachment = tracer.extract(invocation);
            String traceId = null;
            if(traceAttachment !=null){
                traceId = traceAttachment.getTraceId();
            }
            if (StringUtils.isEmpty(traceId)) {
                traceId = traceIdGenerator.getTraceId(invocation);
            }
            return new Trace(traceId);
        }

        public static Trace build(Invocation invocation,TraceIdGenerator traceIdGenerator){
            return new Trace(traceIdGenerator.getTraceId(invocation));
        }
    }

    public String getTraceId() {
        return traceId;
    }

    public void getParentId() {

    }

    public void getRootId() {

    }


    private String getTraceIdParameterName() {

        return DEFAULT_TRACE_MDC_PARAMETER_NAME;
    }



    public void clear() {
        traceThreadLocal.set(null);
        MDC.clear();
    }


}

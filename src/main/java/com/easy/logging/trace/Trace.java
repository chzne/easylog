package com.easy.logging.trace;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.easy.logging.Invocation;
import com.easy.logging.TraceIdGenerator;
import com.easy.logging.Tracer;
import org.slf4j.MDC;

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

        public static Trace build(Tracer tracer, Invocation invocation){
            Attachment attachment = tracer.extract(invocation);
            String traceId = null;
            if(attachment!=null){
                traceId = attachment.getTraceId();
            }
            if (StringUtils.isEmpty(traceId)) {
                traceId = tracer.getIdGenerator().getTraceId();
            }
            return new Trace(traceId);
        }

        public static Trace build(TraceIdGenerator traceIdGenerator){
            return new Trace(traceIdGenerator.getTraceId());
        }
    }



    public static Trace getConcurrentTrace() {

        Trace trace = traceThreadLocal.get();
        return trace;
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

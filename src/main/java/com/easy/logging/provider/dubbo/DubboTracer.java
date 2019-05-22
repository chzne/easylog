package com.easy.logging.provider.dubbo;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.dubbo.rpc.RpcContext;
import com.easy.logging.TraceIdGenerator;
import com.easy.logging.Tracer;
import com.easy.logging.trace.Attachment;

import java.util.Map;

public class DubboTracer implements Tracer<DubboInvocationAdapter> {

    private TraceIdGenerator traceIdGenerator;

    public final static String DUBBO_TRACE_ID_KEY = "LOG_TRACE_ID";

    protected String traceParameterName=DUBBO_TRACE_ID_KEY;

    @Override
    public void setIdGenerator(TraceIdGenerator traceIdGenerator) {
        this.traceIdGenerator= traceIdGenerator;
    }

    @Override
    public TraceIdGenerator getIdGenerator() {
        return traceIdGenerator;
    }

    @Override
    public Attachment extract(DubboInvocationAdapter invocation) {
        Map<String, String> map = invocation.getAttachments();
        String traceId = map.get(getTraceParameterName());
        if (StringUtils.isEmpty(traceId)) {
            return null;
        }
        Attachment attachment = new Attachment(traceId);
        return attachment;
    }

    @Override
    public boolean canExtract(DubboInvocationAdapter invocation) {
        RpcContext rpcContext = RpcContext.getContext();
        boolean isProviderSide = rpcContext.isProviderSide();
        return isProviderSide;
    }

    @Override
    public boolean canInject(DubboInvocationAdapter invocation) {
        RpcContext rpcContext = RpcContext.getContext();
        boolean isConsumerSide = rpcContext.isConsumerSide();
        return isConsumerSide;
    }

    @Override
    public void inject(DubboInvocationAdapter invocation, Attachment carrier) {
        if(carrier!=null){
            Map<String, String> map = invocation.getAttachments();
            map.put(DUBBO_TRACE_ID_KEY, carrier.getTraceId());
        }

    }

    public void setTraceParameterName(String traceParameterName){
        this.traceParameterName=traceParameterName;
    }

    public String getTraceParameterName(){
        return traceParameterName;
    }

}

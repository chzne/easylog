package com.easy.logging;

public class TraceAttachment {

    protected String traceId;

    public TraceAttachment(String traceId) {
        this.traceId = traceId;
    }


    public String getTraceId() {
        return traceId;
    }

}

package com.easy.logging.trace;

public class Attachment {

    protected String traceId;

    public Attachment(String traceId) {
        this.traceId = traceId;
    }


    public String getTraceId() {
        return traceId;
    }

}

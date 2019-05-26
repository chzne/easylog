package com.easy.logging;

/**
 *
 * there are two functions for tracing:
 * extract: extract the attachment from up server that usually is a service consumer
 * inject: inject a attachment to down server that usually is a service provider
 *
 * */
public interface Tracer<T extends Invocation> {

    public boolean canExtract(T invocation);

    public TraceAttachment extract(T invocation);

    public boolean canInject(T invocation);

    public void inject(T invocation, TraceAttachment traceAttachment);
}

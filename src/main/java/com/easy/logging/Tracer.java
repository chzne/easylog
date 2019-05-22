package com.easy.logging;

import com.easy.logging.trace.Attachment;

/**
 * trace the chain of invocation across servers
 * there are two functions for tracing:
 * extract: extract the attachment from up server that usually is a service consumer
 * inject: inject a attachment to down server that usually is a service provider
 *
 * */
public interface Tracer<T extends Invocation> {

    public void setIdGenerator(TraceIdGenerator traceIdGenerator);

    public TraceIdGenerator getIdGenerator();

    public boolean canExtract(T invocation);

    public Attachment extract(T invocation);

    public boolean canInject(T invocation);

    public void inject(T invocation, Attachment attachment);
}

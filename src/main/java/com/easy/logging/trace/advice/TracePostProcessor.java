package com.easy.logging.trace.advice;

import com.easy.logging.*;
import com.easy.logging.PostProcessor;
import com.easy.logging.session.SessionClosedEvent;

import com.easy.logging.session.SessionCreatedEvent;
import com.easy.logging.TraceAttachment;
import com.easy.logging.Trace;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
@Slf4j
public class TracePostProcessor implements PostProcessor, Ordered ,SessionListener {

    protected final static String TRACE_CREATION_FLAG="trace_creation_invocation";
    protected final static String TRACE_CREATION_FLAG_VALUE="YES";

    private final TracerRegistry tracerRegistry;
    private final TraceIdGenerator traceIdGenerator;

    public TracePostProcessor(TracerRegistry tracerRegistry,TraceIdGenerator traceIdGenerator) {
        this.tracerRegistry = tracerRegistry;
        this.traceIdGenerator = traceIdGenerator;
    }

    @Override
    public void created(SessionCreatedEvent se) {
        Session session = se.getSession();
        Invocation invocation = se.getInvocation();
        Tracer tracer = tracerRegistry.getTracer(invocation.getClass());
        Trace trace = SessionManager.SessionHolder.getSession().getTrace();
        if(tracer!=null){
            if(trace==null){
                if(tracer.canExtract(invocation)){
                    TraceAttachment traceAttachment = tracer.extract(invocation);
                    if(traceAttachment !=null){
                        trace = Trace.TraceBuilder.build(tracer,invocation,traceIdGenerator);
                    }else{
                        trace = Trace.TraceBuilder.build(invocation,traceIdGenerator);
                    }
                }
            }
        }else{
            trace = Trace.TraceBuilder.build(invocation,traceIdGenerator);
        }
        session.setTrace(trace);
    }

    @Override
    public void closed(SessionClosedEvent se) {

    }




    @Override
    public void before(Invocation invocation) {
        Tracer tracer = tracerRegistry.getTracer(invocation.getClass());
        Trace trace = SessionManager.SessionHolder.getSession().getTrace();
        if(trace!=null && tracer!=null){
            if(tracer.canInject(invocation)){
                TraceAttachment traceAttachment = new TraceAttachment(trace.getTraceId());
                tracer.inject(invocation, traceAttachment);
            }
        }
        // Trace trace  = Trace.TraceBuilder.build(traceIdGenerator,invocation);

    }

    @Override
    public void after(Invocation invocation, Object result) {
        /*Object object = invocation.getAttribute(TRACE_CREATION_FLAG);
        if(object!=null && TRACE_CREATION_FLAG.equals(object)){
            Trace trace = Trace.getConcurrentTrace();
            if(trace!=null){
                trace.clear();
            }
        }*/
    }

    @Override
    public void throwing(Invocation invocation, Throwable throwable) {
        after(invocation,throwable);
    }


    @Override
    public int getOrder() {
        return -9000;
    }


}

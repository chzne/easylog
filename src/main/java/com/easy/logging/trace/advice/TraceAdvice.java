package com.easy.logging.trace.advice;

import com.easy.logging.Advice;
import com.easy.logging.Tracer;
import com.easy.logging.trace.Attachment;
import com.easy.logging.trace.Trace;
import com.easy.logging.Invocation;
import com.easy.logging.TracerRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
@Slf4j
public class TraceAdvice implements Advice, Ordered {

    protected final static String TRACE_CREATION_FLAG="trace_creation_invocation";
    protected final static String TRACE_CREATION_FLAG_VALUE="YES";

    private final TracerRegistry tracerRegistry;

    public TraceAdvice(TracerRegistry tracerRegistry) {
        this.tracerRegistry = tracerRegistry;
    }

    protected Tracer getTracer(Invocation invocation){
        Tracer tracer = tracerRegistry.getTracer(invocation.getClass());
        if(tracer!=null){
            return tracer;
        }
        return null;
    }


    @Override
    public void before(Invocation invocation) {

        Tracer tracer = getTracer(invocation);
        if(tracer!=null){
            Trace trace = Trace.getConcurrentTrace();
            if(trace==null){
                if(tracer.canExtract(invocation)){
                    Attachment attachment = tracer.extract(invocation);
                    if(attachment!=null){
                        Trace.TraceBuilder.build(tracer,invocation);
                    }else{
                        Trace.TraceBuilder.build(tracer.getIdGenerator());
                    }
                    invocation.setAttribute(TRACE_CREATION_FLAG,TRACE_CREATION_FLAG_VALUE);
                }

            }else{
                if(tracer.canInject(invocation)){
                    Attachment attachment = new Attachment(trace.getTraceId());
                    tracer.inject(invocation,attachment);
                }

            }
        }



       // Trace trace  = Trace.TraceBuilder.build(traceIdGenerator,invocation);

    }

    @Override
    public void after(Invocation invocation, Object result) {
        Object object = invocation.getAttribute(TRACE_CREATION_FLAG);
        if(object!=null && TRACE_CREATION_FLAG.equals(object)){
            Trace trace = Trace.getConcurrentTrace();
            if(trace!=null){
                trace.clear();
            }
        }
    }

    @Override
    public void throwing(Invocation invocation, Throwable throwable) {
        after(invocation,throwable);
    }

    /**
     * Get the order value of this object.
     * <p>Higher values are interpreted as lower priority. As a consequence,
     * the object with the lowest value has the highest priority (somewhat
     * analogous to Servlet {@code load-on-startup} values).
     * <p>Same order values will result in arbitrary sort positions for the
     * affected objects.
     *
     * @return the order value
     * @see #HIGHEST_PRECEDENCE
     * @see #LOWEST_PRECEDENCE
     */
    @Override
    public int getOrder() {
        return -9000;
    }
}

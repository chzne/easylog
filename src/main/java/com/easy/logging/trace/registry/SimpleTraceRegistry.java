package com.easy.logging.trace.registry;

import com.easy.logging.Tracer;
import com.easy.logging.TracerRegistry;
import com.easy.logging.Invocation;

import java.util.HashMap;

public class SimpleTraceRegistry implements TracerRegistry<Invocation> {

    protected HashMap<Class<Invocation>, Tracer<Invocation>> tracerHashMap = new HashMap<>();

    @Override
    public void registor(Class<Invocation> invocation, Tracer<Invocation> tracer) {
        tracerHashMap.put(invocation,tracer);
    }

    @Override
    public Tracer<Invocation> getTracer(Class<Invocation> invocation) {
        return tracerHashMap.get(invocation);
    }
}

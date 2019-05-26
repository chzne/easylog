package com.easy.logging.trace.registry;

import com.easy.logging.Tracer;
import com.easy.logging.TracerRegistry;
import com.easy.logging.Invocation;

import java.util.HashMap;

public class SimpleTracerRegistry<T extends Invocation> implements TracerRegistry<T> {

    protected HashMap<Class<T>, Tracer<T>> tracerHashMap = new HashMap<>();

    @Override
    public void register(Class<T> invocation, Tracer<T> tracer) {
        tracerHashMap.put(invocation,tracer);
    }

    @Override
    public Tracer<T> getTracer(Class<T> invocation) {
        return tracerHashMap.get(invocation);
    }
}

package com.easy.logging;

public interface TracerRegistry<T extends Invocation> {

    public void register(Class<T> invocation, Tracer<T> tracer);

    public Tracer<T>  getTracer(Class<T> invocation);

}

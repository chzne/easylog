package com.easy.logging.trace.generator;

import com.easy.logging.Invocation;
import com.easy.logging.TraceIdGenerator;

import java.util.concurrent.atomic.AtomicLong;

public class IncrementTraceIdGenerator implements TraceIdGenerator {

    protected final AtomicLong atomicLong = new AtomicLong(System.currentTimeMillis());

    @Override
    public String getTraceId(Invocation invocation) {

        return String.valueOf(atomicLong.incrementAndGet());
    }
}

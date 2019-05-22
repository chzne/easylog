package com.easy.logging.provider.logback;

import ch.qos.logback.classic.spi.LoggingEvent;
import ch.qos.logback.core.OutputStreamAppender;
import ch.qos.logback.core.spi.FilterReply;
import com.easy.logging.trace.Trace;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j
public class MemoryConsoleAppender extends OutputStreamAppender<LoggingEvent> {

    protected static ConcurrentHashMap<String, ByteArrayOutputStream> map = new ConcurrentHashMap<>();

    public static final String request_key = "traceId";

    public static ReentrantLock lock = new ReentrantLock();

    public MemoryConsoleAppender() {
        // addFilter(new RequestContextMDCFilter());
    }

    @Override
    public OutputStream getOutputStream() {

        String traceId = Trace.getConcurrentTrace().getTraceId();
        if (traceId == null) {
            traceId="empty";

        }
        ByteArrayOutputStream outputStream = map.get(traceId);
        if (outputStream == null) {
            outputStream = new ByteArrayOutputStream();
            map.put(traceId, outputStream);
        }
        return outputStream;
    }



    @Override
    public FilterReply getFilterChainDecision(LoggingEvent event) {
        return super.getFilterChainDecision(event);
    }
}

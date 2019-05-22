package com.easy.logging.provider.logback;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.pattern.EnsureExceptionHandling;
import ch.qos.logback.classic.pattern.ExtendedThrowableProxyConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Context;
import ch.qos.logback.core.pattern.Converter;
import ch.qos.logback.core.pattern.ConverterUtil;

public class TraceEnsureExceptionHandling extends EnsureExceptionHandling {

    @Override
    public void process(Context context, Converter<ILoggingEvent> head) {
        if (head == null) {
            // this should never happen
            throw new IllegalArgumentException("cannot process empty chain");
        }
        if (!chainHandlesThrowable(head)) {
            Converter<ILoggingEvent> tail = ConverterUtil.findTail(head);
            Converter<ILoggingEvent> exConverter = null;
            LoggerContext loggerContext = (LoggerContext) context;
            if (loggerContext.isPackagingDataEnabled()) {
                exConverter = new ExtendedThrowableProxyConverter();
            } else {
                exConverter = new TraceThrowableProxyConverter();
            }
            tail.setNext(exConverter);
        }
    }
}

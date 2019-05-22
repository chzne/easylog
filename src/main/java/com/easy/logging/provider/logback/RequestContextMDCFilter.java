package com.easy.logging.provider.logback;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.filter.LevelFilter;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.spi.FilterReply;

public class RequestContextMDCFilter extends LevelFilter {

    public static final String request_key = "traceId";

    @Override
    public FilterReply decide(ILoggingEvent event) {

        if (event.getLevel().equals(Level.ERROR)) {
            String str = new String();
        }

        return super.decide(event);
    }


}

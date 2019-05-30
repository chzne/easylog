package com.easy.logging.provider.logback;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.pattern.ThrowableProxyConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;
import com.easy.logging.Session;
import com.easy.logging.SessionManager;

import com.easy.logging.Trace;

public class TraceThrowableProxyConverter extends ThrowableProxyConverter {

    @Override
    public String convert(ILoggingEvent event) {
        if (event.getLevel().equals(Level.ERROR)) {
            String msg = super.convert(event);
            if(msg==null || msg.isEmpty()){
                 msg = event.getFormattedMessage();
            }
            Session session = SessionManager.SessionHolder.getSession();
            Trace trace;
            if(session!=null){
                trace  = session.getTrace();
                if(trace!=null){
                    String msgWithTraceId = msg.replaceAll("\n", "\n" + trace.getTraceId());
                    return trace.getTraceId()+" "+msgWithTraceId;
                }
            }

        }
        return super.convert(event);
    }
}

package com.easy.logging.monitor;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;
import com.easy.logging.Session;
import com.easy.logging.SessionManager;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;

public class MemoryAppender  extends AppenderBase<ILoggingEvent>  {

    protected static ConcurrentHashMap<String,List<String>> logMap = new ConcurrentHashMap<>();

    protected static ConcurrentSkipListSet<String> errorTraceIdSet = new ConcurrentSkipListSet<>();


    @Override
    protected void append(ILoggingEvent e) {
        Session session = SessionManager.SessionHolder.getSession();
        if(session!=null){
            String traceId = session.getTrace().getTraceId();
            if(!logMap.containsKey(traceId)){
                logMap.putIfAbsent(traceId,new LinkedList<>());
            }else{
                if(e.getLevel().equals(Level.ERROR)){
                   if(!errorTraceIdSet.contains(traceId)){
                       errorTraceIdSet.add(traceId);
                   }
                }
                logMap.get(traceId).add(e.getFormattedMessage());
            }
        }
    }

    public static boolean hasError(String traceId){
        return errorTraceIdSet.contains(traceId);

    }

    public static List<String> getLogs(String traceId){
        List<String> list = logMap.get(traceId);
        return list;
    }

    public static void remove(String traceId){
        logMap.remove(traceId);
        errorTraceIdSet.remove(traceId);

    }


}

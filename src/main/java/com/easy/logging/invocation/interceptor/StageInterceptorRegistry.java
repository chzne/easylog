package com.easy.logging.invocation.interceptor;

import com.easy.logging.InvocationStage;
import com.easy.logging.invocation.InterceptorRegistry;
import com.easy.logging.InvocationPostProccessorInterceptor;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class StageInterceptorRegistry implements InterceptorRegistry {

    protected Map<InvocationStage,List<InvocationPostProccessorInterceptor>> map = new ConcurrentHashMap<>();
    @Override
    public void register(InvocationStage invocationStage, InvocationPostProccessorInterceptor invocationPostProccessorInterceptor) {
        List<InvocationPostProccessorInterceptor> list = map.get(invocationStage);
        if(list==null){
            list = new LinkedList<>();
            map.put(invocationStage,list);
        }
        list.add(invocationPostProccessorInterceptor);
    }

    @Override
    public List<InvocationPostProccessorInterceptor> getInvocationInterceptor(InvocationStage invocationStage) {
        return map.get(invocationStage);
    }
}

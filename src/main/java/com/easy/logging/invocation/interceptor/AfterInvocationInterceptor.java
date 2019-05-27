package com.easy.logging.invocation.interceptor;

import com.easy.logging.InvocationStage;
import com.easy.logging.InvocationInterceptor;

public abstract class AfterInvocationInterceptor implements InvocationInterceptor {

    @Override
    public final InvocationStage[] getInvocationStage(){

        return new InvocationStage[]{InvocationStage.AFTER};

    }
}

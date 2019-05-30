package com.easy.logging.invocation.interceptor;

import com.easy.logging.InvocationStage;
import com.easy.logging.InvocationPostProccessorInterceptor;

public abstract class BeforeInvocationPostProccessorInterceptor implements InvocationPostProccessorInterceptor {

    @Override
    public final InvocationStage[] getInvocationStage(){

        return new InvocationStage[]{InvocationStage.BEFORE};

    }
}

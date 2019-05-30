package com.easy.logging;

import com.easy.logging.Invocation;
import com.easy.logging.InvocationStage;
import com.easy.logging.Session;

import java.util.List;

public interface InvocationPostProccessorInterceptor {

    public boolean intercept(Session session, Invocation invocation);

    public InvocationStage[] getInvocationStage();
}

package com.easy.logging.invocation.interceptor.exception;

import com.easy.logging.Invocation;
import com.easy.logging.Session;
import com.easy.logging.invocation.interceptor.ExceptionInvocationPostProccessorInterceptor;


public class PropagatingExceptionPostProccessorInterceptor extends ExceptionInvocationPostProccessorInterceptor {

    protected final static String DEFAULT_SESSION_EXCEPTION_ATTRIBUTE_NAME="DEFAULT_SESSION_EXCEPTION_ATTRIBUTE_NAME";

    @Override
    public boolean intercept(Session session, Invocation invocation) {

        Object exception = session.getAttribute(DEFAULT_SESSION_EXCEPTION_ATTRIBUTE_NAME);
        if(exception!=null){

            return true;
        }
        session.setAttribute(DEFAULT_SESSION_EXCEPTION_ATTRIBUTE_NAME,"true");
        return false;
    }
}

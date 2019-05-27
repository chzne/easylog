package com.easy.logging.invocation.interceptor;

import com.easy.logging.Invocation;
import com.easy.logging.Session;


public class PropagatingExceptionInterceptor extends ExceptionInvocationInterceptor {

    protected final static String LAST_EXCEPTION_DEFAULT_ATTRIBUTE_NAME="last_exception_default_attribute_name";


    @Override
    public boolean intercept(Session session, Invocation invocation) {
     //   Object exception = session.getAttribute("LAST_EXCEPTION_DEFAULT_ATTRIBUTE_NAME");
        return false;
    }
}

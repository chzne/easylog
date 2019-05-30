package com.easy.logging.invocation;

import java.lang.reflect.Method;

public class MethodInvocation extends AbstractInvocation {

    public MethodInvocation(Class<?> target, Method method, Object[] argurments) {
        setTarget(target);
        setMethod(method);
        setArguments(argurments);
    }


    @Override
    public Object doInvoke() throws Throwable {
        return getMethod().invoke(getTarget(), getArguments());
    }
}

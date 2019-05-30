package com.easy.logging.provider.web.mvc;

import com.easy.logging.invocation.InvocationAdapterFactory;
import com.easy.logging.invocation.adapter.InvocationAdapter;
import org.aopalliance.intercept.MethodInvocation;

public class MvcInvocationAdapterFactory implements InvocationAdapterFactory {
    @Override
    public InvocationAdapter getInstance(Object... objects) {
        MethodInvocation methodInvocation= (MethodInvocation) objects[0];

        return null;
    }
}

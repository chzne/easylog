package com.easy.logging.invocation;

import com.easy.logging.Session;
import com.easy.logging.invocation.adapter.InvocationAdapter;

public interface InvocationAdapterFactory<MethodInvocation> {

    public InvocationAdapter getInstance(Object... objects);
}

package com.easy.logging.provider.dubbo;

import com.alibaba.dubbo.rpc.Invoker;
import com.alibaba.dubbo.rpc.RpcException;
import com.alibaba.dubbo.rpc.RpcInvocation;
import com.easy.logging.invocation.adapter.InvocationAdapter;

import java.lang.reflect.Method;
import java.util.Map;

public class DubboInvocationAdapter extends InvocationAdapter {

    private final Invoker<?> invoker;
    private final RpcInvocation rpcInvocation;

    public DubboInvocationAdapter(Invoker<?> invoker, RpcInvocation rpcInvocation)  {
        setTarget(invoker.getInterface());
        setArgurments(rpcInvocation.getArguments());
        try {
            Method method = invoker.getInterface().getMethod(rpcInvocation.getMethodName(), rpcInvocation.getParameterTypes());
            setMethod(method);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        this.invoker = invoker;
        this.rpcInvocation = rpcInvocation;

    }

    public Map<String, String> getAttachments(){
        return rpcInvocation.getAttachments();
    }

    @Override
    public Object proceed() throws RpcException {
        return invoker.invoke(rpcInvocation);
    }


}

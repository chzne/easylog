package com.easy.logging.provider.dubbo;


import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.extension.Activate;
import com.alibaba.dubbo.rpc.*;
import com.easy.logging.Advisor;
import com.easy.logging.InvocationDelegator;

@Activate(group = {Constants.CONSUMER,Constants.PROVIDER})
public class DubboAdvisor implements Filter, Advisor {


    private InvocationDelegator invocationDelegator;

    public void setInvocationDelegator(InvocationDelegator delegator) {

        this.invocationDelegator = delegator;
    }


    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {

        DubboInvocationAdapter adapter = new DubboInvocationAdapter(invoker, (RpcInvocation) invocation);
        try {
            return (Result) invocationDelegator.invoke(adapter);
        } catch (Throwable throwable) {

        }
        return null;


    }
}

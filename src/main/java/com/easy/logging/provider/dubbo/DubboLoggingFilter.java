package com.easy.logging.provider.dubbo;


import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.extension.Activate;
import com.alibaba.dubbo.rpc.*;
import com.easy.logging.Advisor;
import com.easy.logging.InvocationProxy;
import com.easy.logging.InvocationProxyHolder;

@Activate(group = {Constants.CONSUMER,Constants.PROVIDER})
public class DubboLoggingFilter implements Filter, Advisor {


    private InvocationProxy invocationProxy;

    private  boolean invocationProxyInit=false;

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {


        if(invocationProxy==null && !invocationProxyInit){
            synchronized (this){
                if(invocationProxy==null && !invocationProxyInit){
                    invocationProxyInit=true;
                    invocationProxy = InvocationProxyHolder.getInvocationProxy();
                }
            }
        }
        if(invocationProxy!=null){
            DubboInvocationAdapter adapter = new DubboInvocationAdapter(invoker, (RpcInvocation) invocation);
            try {
                return (Result) invocationProxy.invoke(adapter);
            } catch (Throwable throwable) {
                throw new RpcException(throwable.getMessage(),throwable);
            }
        }
        return invoker.invoke(invocation);


    }
}

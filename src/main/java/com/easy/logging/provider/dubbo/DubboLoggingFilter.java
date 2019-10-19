package com.easy.logging.provider.dubbo;


import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.extension.Activate;
import com.alibaba.dubbo.rpc.*;
import com.easy.logging.Advisor;
import com.easy.logging.InvocationProxy;
import com.easy.logging.InvocationProxyHolder;


public class DubboLoggingFilter implements Filter, Advisor {




    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {



        return invoker.invoke(invocation);


    }
}

package com.easy.logging.provider.dubbo;

import com.alibaba.dubbo.common.json.JSON;
import com.alibaba.dubbo.common.json.JSONConverter;
import com.alibaba.dubbo.common.json.JSONObject;
import com.alibaba.dubbo.rpc.RpcResult;
import com.easy.logging.Invocation;
import com.easy.logging.InvocationLogger;
import com.easy.logging.logging.config.InvocationLoggingConfig;
import com.easy.logging.logging.logger.GenericInvocationLogger;
import com.easy.logging.logging.logger.Message;
import org.springframework.boot.json.GsonJsonParser;

import java.io.IOException;

public class DubboInvocationLogger extends GenericInvocationLogger<DubboInvocationAdapter> {
    public DubboInvocationLogger(InvocationLoggingConfig config) {
        super(config);
    }

    @Override
    public Message getResultMessage(DubboInvocationAdapter invocation, Object result) {
        RpcResult data = (RpcResult) result;
        if(data.getResult()!=null){
            Object jsonStr = null;
            try {
                 jsonStr = JSON.json( data );
            } catch (IOException e) {
                e.printStackTrace();
            }
            return super.getResultMessage(invocation,jsonStr);
        }
        return super.getResultMessage(invocation, result);
    }

    @Override
    public int getPriority(Class<? extends Invocation> invocation) {
        return super.getPriority(invocation)-43;
    }

    @Override
    public Class<Invocation>[] getSupportedInvocationType() {
        return new Class[]{DubboInvocationAdapter.class};
    }
}

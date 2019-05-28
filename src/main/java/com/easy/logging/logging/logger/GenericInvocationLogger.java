package com.easy.logging.logging.logger;

import com.easy.logging.Invocation;
import com.easy.logging.InvocationLogger;
import com.easy.logging.logging.config.InvocationLoggingConfig;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GenericInvocationLogger<T extends Invocation> extends AbstractInvocationLogger<T> {

    public GenericInvocationLogger(InvocationLoggingConfig config) {
        super(config);
    }

    @Override
    public Message getArgurmentsMessage(T invocation) {
        String method = invocation.getMethod().getName();
        String placeHolder = getPlaceHolder(invocation.getArgurments().length);
        String format = method+"("+placeHolder+")";
        return new Message(format,invocation.getArgurments());
    }

    @Override
    public Message getResultMessage(T invocation, Object result) {

        String format ="[{}]";
        Class<?> type = invocation.getMethod().getReturnType();
        if(type.getName().equals("void")){
            format =  "[void]";
        }
        return new Message(format,result);
    }



    protected String getPlaceHolder(int num){
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < num; i++) {
            stringBuilder.append("{}");
            if(i!=num-1){
                stringBuilder.append(",");
            }
        }
        return stringBuilder.toString();
    }

    @Override
    public int getPriority(Class<? extends Invocation> invocation) {

        return 0;
    }

    @Override
    public Class<Invocation>[] getSupportedInvocationType() {
        return new Class[]{Invocation.class};
    }








}

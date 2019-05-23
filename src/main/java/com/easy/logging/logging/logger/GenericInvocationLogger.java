package com.easy.logging.logging.logger;

import com.easy.logging.Invocation;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GenericInvocationLogger<T extends Invocation> extends AbstractInvocationLogger<T> {

    protected final static String DEFAULT_INVOCATION_BEFORE_LOG_PREFIX="start:";

    protected final static String DEFAULT_INVOCATION_AFTER_LOG_PREFIX="end:";

    private String beforePrefix = DEFAULT_INVOCATION_BEFORE_LOG_PREFIX;

    private String afterPrefix=DEFAULT_INVOCATION_AFTER_LOG_PREFIX;

    public String getBeforePrefix() {
        return beforePrefix;
    }

    public String getAfterPrefix() {
        return afterPrefix;
    }

    public void setBeforePrefix(String prefix){
        this.beforePrefix= prefix;

    }

    public void setAfterPrefix(String prefix){
        this.afterPrefix= prefix;

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
    public Message beforeMessage(T invocation) {
        String method = invocation.getMethod().getName();
        String placeHolder = getPlaceHolder(invocation.getArgurments().length);
        //return super.getFormattedMessage(loggingInvocation);
        String format = method+"("+placeHolder+")";
        return new Message(getBeforePrefix()+format,invocation.getArgurments());

    }

    @Override
    public Message afterMessage(T invocation,Object result) {
        String format ="return:[{}]";
        Class<?> type = invocation.getMethod().getReturnType();
        if(type.getName().equals("void")){
            format =  "return:[void]";
        }
        return new Message(getAfterPrefix()+format,result);

    }


    @Override
    public int getPriority(Class<? extends Invocation> invocation) {

        return 0;
    }

    @Override
    public void throwing(T invocation, Throwable throwable) {
        String format = "发生异常:";
        String message ="";
        if(throwable instanceof Exception){
            Exception exception = (Exception) throwable;

            if(exception.getMessage()==null){
                message ="异常类:"+exception.getClass()+"事发地:"+throwable.getStackTrace()[0].toString();
            }

            message ="异常类:"+exception.getClass()+"异常消息:"+exception.getMessage()+"事发地:"+throwable.getStackTrace()[0].toString();

        }

        delegate(invocation,format,message,"");
    }



}

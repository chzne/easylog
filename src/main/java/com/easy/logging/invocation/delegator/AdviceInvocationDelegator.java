package com.easy.logging.invocation.delegator;

import com.easy.logging.Advice;
import com.easy.logging.ExceptionHolder;
import com.easy.logging.Invocation;
import com.easy.logging.InvocationDelegator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;

import java.util.Arrays;
import java.util.List;

import static com.easy.logging.Invocation.END_TIME_ATTRIBUTE_KEY;
import static com.easy.logging.Invocation.START_TIME_ATTRIBUTE_KEY;


@Slf4j
public class AdviceInvocationDelegator implements InvocationDelegator {

    private final Advice[] advices;


    public AdviceInvocationDelegator(ObjectProvider<Advice[]> advices){
        this.advices = advices.getIfAvailable();
    }

    @Override
    public List<Advice> getAdvices() {
        return Arrays.asList(advices);
    }

    @Override
    public Object invoke(Invocation invocation) throws Throwable {

        boolean throughException = false;
        Object result=null;
        try{
            invocation.setAttribute(START_TIME_ATTRIBUTE_KEY,System.currentTimeMillis());
            for (int i = 0; i < advices.length; i++) {
                advices[i].before(invocation);
            }
            result = invocation.proceed();
            invocation.setAttribute(END_TIME_ATTRIBUTE_KEY,System.currentTimeMillis());
            return result;

        }catch (Throwable ex){
            throughException=true;
            invocation.setAttribute(END_TIME_ATTRIBUTE_KEY,System.currentTimeMillis());
            ExceptionHolder exceptionHolder = ExceptionHolder.currentExceptionHolder();
            if(!exceptionHolder.hasException()){
                exceptionHolder.setException(ex);
                for (int i =  advices.length-1; i >=0; i--) {
                    advices[i].throwing(invocation,ex);
                }
            }
            throw ex;

        }finally {
            if(!throughException){
                for (int i =  advices.length-1; i >=0; i--) {
                    advices[i].after(invocation,result);
                }
            }
        }
    }
}

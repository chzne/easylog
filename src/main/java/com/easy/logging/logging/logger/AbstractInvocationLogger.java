package com.easy.logging.logging.logger;

import com.easy.logging.InvocationLogger;
import com.easy.logging.Invocation;
import com.easy.logging.Session;
import com.easy.logging.Trace;
import com.easy.logging.logging.config.InvocationLoggingConfig;
import com.easy.logging.session.SessionHolder;
import com.easy.logging.spring.annotation.Logging;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
@Data
public abstract class AbstractInvocationLogger<T extends Invocation> implements InvocationLogger<T> {


    private final InvocationLoggingConfig config;

    public AbstractInvocationLogger(InvocationLoggingConfig config){
        this.config = config;
    }

    public abstract Message getArgurmentsMessage(T invocation);

    public abstract Message getResultMessage(T invocation,Object result);




    @Override
    public final void before(T invocation) {
        if(config.isIncludeArgurments()){
            Message message = getArgurmentsMessage(invocation);
            doLogging(invocation,config.getBeforePrefix()+message.getFormat()+config.getBeforeSuffix(),message.getMessage(),"");
        }
    }

    protected String getElapsedTimeMessage(T invocation){
        long start = invocation.getStartTime();
        long end =   invocation.getEndTime();
        long duration = end - start;
        String elapsedTimeMessage = config.getElapsedTimePrefix() + " " + duration + "ms" + config.getElapsedTimeSuffix();
        return elapsedTimeMessage;
    }

    @Override
    public final void after(T invocation, Object result) {
        if(config.isIncludeResult()){
            Message message = getResultMessage(invocation,result);
            String elapsedTimeMessage ="";
            if(config.isIncludeElapsedTime()){
                elapsedTimeMessage = getElapsedTimeMessage(invocation);
            }
            doLogging(invocation,config.getAfterPrefix()+message.getFormat()+config.getAfterSuffix(),message.getMessage(),elapsedTimeMessage);
        }
    }

    @Override
    public final void throwing(T invocation, Throwable throwable) {

        if(config.isIncludeException()){

            String elapsedTimeMessage ="";
            if(config.isIncludeElapsedTime()){
                elapsedTimeMessage = getElapsedTimeMessage(invocation);
            }

            if(throwable instanceof Exception){
                Exception exception = (Exception) throwable;
                doLogging(invocation,config.getExceptionPrefix(),"","");
                doLogging(invocation,config.getExceptionClassPrefix()+"{}",exception.getClass(),"");
                doLogging(invocation,config.getExceptionMessagePrefix()+"{}",exception.getMessage(),"");
                doLogging(invocation,config.getExceptionLocationPrefix()+"{}",throwable.getStackTrace()[0].toString(),elapsedTimeMessage);
            }



        }

    }

    protected Logger getLogger(Invocation invocation){
        Logger logger = LoggerFactory.getLogger(invocation.getTarget());
        return logger;
    }

    protected void doLogging(T invocation,String format,Object args,String append){

        Logger log = getLogger(invocation);
        if(invocation.getMethod()!=null && config.isIncludeLoggingAnnotationValue()){
            Logging logging= invocation.getMethod().getAnnotation(Logging.class);
            if(logging!=null && logging.value()!=null ){
                format=logging.value()+" "+format;
            }
        }
        if(args!=null && args.getClass().isArray()){
            Object[]  message = (Object[]) args;
            int number =message.length;
            if(number==1){
                 log.info(format+append,message);
            }else if(number==2){
                 log.info(format+append,message[0],message[1]);
            }else if(number==3){
                 log.info(format+append,message[0],message[1],message[2]);
            }else if(number==4){
                 log.info(format+append,message[0],message[1],message[2],message[3]);
            }else if(number==5){
                 log.info(format+append,message[0],message[1],message[2],message[3],message[4]);
            }else if(number==6){
                 log.info(format+append,message[0],message[1],message[2],message[3],message[4],message[5]);
            }else if(number==7){
                 log.info(format+append,message[0],message[1],message[2],message[3],message[4],message[5],message[6]);
            }else if(number==8){
                 log.info(format+append,message[0],message[1],message[2],message[3],message[4],message[5],message[6],message[7]);
            }else{
                 log.info(format+append,message);
            }

        }else{
             log.info(format+append,args);
        }
    }
}

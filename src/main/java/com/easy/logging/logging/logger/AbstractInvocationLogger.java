package com.easy.logging.logging.logger;

import com.easy.logging.Invocation;
import com.easy.logging.InvocationLogger;
import com.easy.logging.SessionManager;
import com.easy.logging.logging.config.InvocationLoggingConfig;
import com.easy.logging.logging.logger.wrapper.LoggerWrapper;
import com.easy.logging.spring.annotation.Exclude;
import com.easy.logging.spring.annotation.Logging;
import io.swagger.annotations.ApiOperation;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;

@Data
public abstract class AbstractInvocationLogger<T extends Invocation> implements InvocationLogger<T> {


    private final InvocationLoggingConfig config;

    private Map<String, LoggerWrapper> loggerCache = new ConcurrentHashMap<>();

    public AbstractInvocationLogger(InvocationLoggingConfig config){
        this.config = config;
    }

    public abstract Message getArgumentsMessage(T invocation);

    public abstract Message getResultMessage(T invocation,Object result);


    protected boolean isExcludeParamInLoggingAnnotation(T invocation,Exclude param){
        Logging logging= invocation.getMethod().getAnnotation(Logging.class);
        if(logging!=null && logging.exclude()!=null ){
            Exclude[] exclution = logging.exclude();
            for (Exclude exclude : exclution) {
                if(exclude.equals(param)){
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public final void before(T invocation) {
        if(config.isIncludeArguments()){
            Message message=new Message("","");
            if(!isExcludeParamInLoggingAnnotation(invocation,Exclude.ARGUMENTS)){
                 message = getArgumentsMessage(invocation);
            }
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
            Message message=new Message("","");
            if(!isExcludeParamInLoggingAnnotation(invocation,Exclude.RESULT)){
                 message = getResultMessage(invocation,result);
            }

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
                doLogging(invocation,config.getExceptionMessagePrefix()+"{}",exception.getMessage(),"",Level.WARNING);
                doLogging(invocation,config.getExceptionLocationPrefix()+"{}",throwable.getStackTrace()[0].toString(),elapsedTimeMessage);
            }



        }

    }

    protected LoggerWrapper getLogger(Invocation invocation){
        if(loggerCache.get(invocation.getTarget().getName())!=null){
            return loggerCache.get(invocation.getTarget().getName());
        }else{
            Logger logger = LoggerFactory.getLogger(invocation.getTarget());
            LoggerWrapper loggerWrapper = new LoggerWrapper(logger);
            loggerCache.put(invocation.getTarget().getName(),new LoggerWrapper(logger));
            return loggerWrapper;

        }


    }

    protected void doLogging(T invocation, String format, Object args, String append, Level level)
    {
        LoggerWrapper log = getLogger(invocation);
        if(invocation.getMethod()!=null && config.isIncludeLoggingAnnotationValue()){
            Logging logging= invocation.getMethod().getAnnotation(Logging.class);
            if(logging!=null && logging.value()!=null ){
                format=logging.value()+" "+format;
            }else{
                ApiOperation apiOperation= invocation.getMethod().getAnnotation(ApiOperation.class);
                if(apiOperation!=null && apiOperation.value()!=null ){

                    format=apiOperation.value()+" "+format;
                }
            }
        }
        if(args!=null && args.getClass().isArray()){
            Object[]  message = (Object[]) args;
            int number =message.length;
            if(number==1){
                if(Level.INFO.equals(level)){
                    log.info(format+append,message);
                }else if(Level.WARNING.equals(level)){
                    log.error(format+append,message);
                }

            }else if(number==2){
                if(Level.INFO.equals(level)){
                    log.info(format+append,message[0],message[1]);
                }else if(Level.WARNING.equals(level)){
                    log.error(format+append,message[0],message[1]);
                }

            }else if(number==3){
                if(Level.INFO.equals(level)){
                    log.info(format+append,message[0],message[1],message[2]);
                }else if(Level.WARNING.equals(level)){
                    log.error(format+append,message[0],message[1],message[2]);
                }

            }else if(number==4){
                if(Level.INFO.equals(level)){
                    log.info(format+append,message[0],message[1],message[2],message[3]);
                }else if(Level.WARNING.equals(level)){
                    log.error(format+append,message[0],message[1],message[2],message[3]);
                }

            }else if(number==5){
                if(Level.INFO.equals(level)){
                    log.info(format+append,message[0],message[1],message[2],message[3],message[4]);
                }else if(Level.WARNING.equals(level)){
                    log.error(format+append,message[0],message[1],message[2],message[3],message[4]);
                }

            }else if(number==6){
                if(Level.INFO.equals(level)){
                    log.info(format+append,message[0],message[1],message[2],message[3],message[4],message[5]);
                }else if(Level.WARNING.equals(level)){
                    log.error(format+append,message[0],message[1],message[2],message[3],message[4],message[5]);
                }

            }else if(number==7){
                if(Level.INFO.equals(level)){
                    log.info(format+append,message[0],message[1],message[2],message[3],message[4],message[5],message[6]);
                }else if(Level.WARNING.equals(level)){
                    log.error(format+append,message[0],message[1],message[2],message[3],message[4],message[5],message[6]);
                }

            }else if(number==8){
                if(Level.INFO.equals(level)){
                    log.info(format+append,message[0],message[1],message[2],message[3],message[4],message[5],message[6],message[7]);
                }else if(Level.WARNING.equals(level)){
                    log.error(format+append,message[0],message[1],message[2],message[3],message[4],message[5],message[6],message[7]);
                }

            }

        }else{
            if(Level.INFO.equals(level)){
                log.info(format+append,args);
            }else if(Level.WARNING.equals(level)){
                log.error(format+append,args);
            }

        }
    }

    protected void doLogging(T invocation,String format,Object args,String append){

        doLogging(invocation,format,args,append,Level.INFO);
    }
}

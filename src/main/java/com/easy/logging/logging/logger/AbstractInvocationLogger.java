package com.easy.logging.logging.logger;

import com.easy.logging.InvocationLogger;
import com.easy.logging.Invocation;
import com.easy.logging.spring.annotation.Logging;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.easy.logging.Invocation.END_TIME_ATTRIBUTE_KEY;
import static com.easy.logging.Invocation.START_TIME_ATTRIBUTE_KEY;

public abstract class AbstractInvocationLogger<T extends Invocation> implements InvocationLogger<T> {

    protected boolean includeLoggingAnnotationValue = true;

    protected boolean includeElapsedTime = true;

    private String elaspedTimeName="cost";


    public void setIncludeLoggingAnnotationValue(boolean include){

        includeLoggingAnnotationValue = include;
    }

    public void setElaspedTimeName(String elaspedTimeName){

        this.elaspedTimeName = elaspedTimeName;
    }

    public String getElaspedTimeName(){

       return elaspedTimeName;
    }

    public void setIncludeElapsedTime(boolean include){

        includeElapsedTime = include;
    }

    public boolean isIncludeElapsedTime(){

        return includeElapsedTime;
    }



    public abstract Message beforeMessage(T invocation);

    public abstract Message afterMessage(T invocation,Object result);

    @Override
    public final void before(T invocation) {
        Message message = beforeMessage(invocation);
        delegate(invocation,message.getFormat(),message.getMessage(),"");
    }

    @Override
    public final void after(T invocation, Object result) {
        Message message = afterMessage(invocation,result);
        String elapsedTime ="";
        if(isIncludeElapsedTime()){
            long start = (long) invocation.getAttribute(START_TIME_ATTRIBUTE_KEY);
            long end = (long) invocation.getAttribute(END_TIME_ATTRIBUTE_KEY);
            long elapsed = end - start;
            elapsedTime="["+getElaspedTimeName()+" "+elapsed+"ms]";
        }

        delegate(invocation,message.getFormat(),message.getMessage(),elapsedTime);

    }

    public boolean getIncludeLoggingAnnotationValue(){

        return includeLoggingAnnotationValue ;

    }

    protected Logger getLogger(Invocation invocation){
        Logger logger = LoggerFactory.getLogger(invocation.getTarget());
        return logger;
    }

    protected void delegate(T invocation,String format,Object args,String append){

        Logger log = getLogger(invocation);
        if(invocation.getMethod()!=null){
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

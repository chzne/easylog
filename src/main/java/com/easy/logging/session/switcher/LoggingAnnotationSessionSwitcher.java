package com.easy.logging.session.switcher;

import com.easy.logging.Invocation;
import com.easy.logging.SessionSwitcher;
import com.easy.logging.spring.annotation.Exclude;
import com.easy.logging.spring.annotation.Logging;

public class LoggingAnnotationSessionSwitcher implements SessionSwitcher {

    protected boolean isExcludeParamInLoggingAnnotation(Invocation invocation, Exclude param){
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
    public boolean tryStop(Invocation invocation) {
        return isExcludeParamInLoggingAnnotation(invocation,Exclude.SESSION);

    }
}

package com.easy.logging.session.switcher;

import com.easy.logging.Invocation;
import com.easy.logging.SessionSwitcher;
import org.springframework.scheduling.annotation.Scheduled;

import java.lang.reflect.Method;

public class SchedulingSessionSwitcher implements SessionSwitcher {
    public SchedulingSessionSwitcher(){

    }

    @Override
    public boolean tryStop(Invocation invocation) {

        Method method = invocation.getMethod();
        if(method!=null){
            Scheduled annotation = method.getAnnotation(Scheduled.class);
            if(annotation!=null){
                return true;
            }
        }
        return false;
    }


}

package com.easy.logging.invocation.proxy;

import com.easy.logging.*;
import com.easy.logging.invocation.InterceptorRegistry;
import com.easy.logging.invocation.adapter.InvocationAdapter;
import com.easy.logging.invocation.advice.OrderedCompositePostProcessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;

import java.util.List;


@Slf4j
public class LoggingInvocationProxy implements InvocationProxy{

    private final InterceptorRegistry interceptorRegistry;

    private final ObjectProvider<SessionSwitcher[]> sessionSwitchers;

    private final SessionManager sessionManager;

    protected OrderedCompositePostProcessor postProcessor;

    public LoggingInvocationProxy(SessionManager sessionManager,OrderedCompositePostProcessor postProcessor, InterceptorRegistry interceptorRegistry, ObjectProvider<SessionSwitcher[]> switchersProviders){

        this.sessionManager = sessionManager;
        this.postProcessor = postProcessor;
        this.interceptorRegistry = interceptorRegistry;
        this.sessionSwitchers = switchersProviders;
    }

    protected boolean interceptInvocationPostProcessor(Session session, Invocation invocation, InvocationStage invocationStage){
        List<InvocationPostProccessorInterceptor> interceptors = interceptorRegistry.getInvocationInterceptor(invocationStage);
        if(interceptors==null){
            return false;
        }
        boolean isIntercept = false;
        for (InvocationPostProccessorInterceptor interceptor : interceptors) {
            isIntercept = interceptor.intercept(session, invocation);
            if(isIntercept){
                break;
            }
        }
        return isIntercept;

    }

    @Override
    public Object invoke(InvocationAdapter invocation) throws Throwable {
        assert invocation!=null;
        Session session = SessionManager.SessionHolder.getSession();
        if(session==null){
            session=sessionManager.open(invocation);
        }
        boolean isLoggingSystemException = false;
        boolean isSessionStoped=false;
        boolean throughException = false;
        Object result = null;
        try {
            try {
                if(!session.isInProcess()){
                    tryRestartSession(invocation, session);
                }
                isSessionStoped = session.isStopped();
                if(!isSessionStoped){
                     isSessionStoped = tryStopSession(invocation, session);
                    if(!isSessionStoped){
                        if(!interceptInvocationPostProcessor(session,invocation,InvocationStage.BEFORE) && postProcessor!=null){
                            postProcessor.before(invocation);
                        }
                    }
                }
            }catch (Exception e){
                isLoggingSystemException = true;
                log.error("EasylogException before the invocation of a method ",e);
            }
            result = invocation.proceed();
        }catch (Throwable throwable) {
             throughException = true;
           if(!isLoggingSystemException){
               if(!isSessionStoped && !interceptInvocationPostProcessor(session,invocation,InvocationStage.EXCEPTION) && postProcessor!=null){
                   postProcessor.throwing(invocation,throwable);
               }
           }
           throw throwable;
        } finally {
            if(!isSessionStoped){
                if(!isLoggingSystemException){
                    if(!throughException){
                        if(!interceptInvocationPostProcessor(session,invocation,InvocationStage.AFTER) && postProcessor!=null){
                            postProcessor.after(invocation,result);
                        }
                    }
                }
            }
            if(session.isHeadInvocation(invocation)){
                sessionManager.close(invocation);
            }
        }
        return result;
    }

    private void tryRestartSession(InvocationAdapter invocation, Session session) {

    }

    private boolean tryStopSession(InvocationAdapter invocation, Session session) {
        SessionSwitcher[] switchers = sessionSwitchers.getIfAvailable();
        if(switchers!=null){
            for (SessionSwitcher switcher : switchers) {
                if(switcher.tryStop(invocation)){
                    session.stop(invocation);
                    return true;
                }
            }
        }
        return false;

    }

}

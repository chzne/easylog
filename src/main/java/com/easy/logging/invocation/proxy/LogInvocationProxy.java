package com.easy.logging.invocation.proxy;

import com.easy.logging.*;
import com.easy.logging.invocation.InterceptorRegistry;
import com.easy.logging.InvocationInterceptor;
import com.easy.logging.InvocationProxy;
import com.easy.logging.invocation.adapter.InvocationAdapter;
import com.easy.logging.invocation.advice.OrderedCompositePostProcessor;
import com.easy.logging.SessionFactory;
import com.easy.logging.session.SessionDestroyedEvent;
import com.easy.logging.session.SessionHolder;
import com.easy.logging.session.SessionStartedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.LinkedList;
import java.util.List;


@Slf4j
public class LogInvocationProxy implements InvocationProxy , ApplicationContextAware {

    private final InterceptorRegistry interceptorRegistry;

    protected OrderedCompositePostProcessor postProcessor;

    protected SessionFactory sessionFactory;

    protected List<SessionListener> sessionListeners = new LinkedList<>();

    private ApplicationContext applicationContext;

    public LogInvocationProxy(SessionFactory sessionFactory,OrderedCompositePostProcessor postProcessor, InterceptorRegistry interceptorRegistry){
        this.sessionFactory = sessionFactory;
        this.postProcessor = postProcessor;
        this.interceptorRegistry = interceptorRegistry;
    }

    @Override
    public void addSessionListener(SessionListener sessionListener){

        sessionListeners.add(sessionListener);
    }


    protected boolean isIntercept(Session session,Invocation invocation,InvocationStage invocationStage){
        List<InvocationInterceptor> interceptors = interceptorRegistry.getInvocationInterceptor(invocationStage);
        if(interceptors==null){
            return false;
        }
        boolean isIntercept = false;
        for (InvocationInterceptor interceptor : interceptors) {
            isIntercept = interceptor.intercept(session, invocation);
            if(isIntercept){
                break;
            }
        }
        return isIntercept;

    }

    @Override
    public Object delegating(InvocationAdapter invocation) throws Throwable {
        assert invocation!=null;
        Session session = SessionHolder.getSession();
        boolean isLoggingSystemException = false;
        boolean throughException = false;
        Object result = null;
        try {
            try {
                if(session==null){
                    session = sessionFactory.getInstance();
                    session.start(invocation);

                    for (SessionListener listener : sessionListeners) {
                        listener.started(new SessionStartedEvent(session,invocation));
                    }
                }
                if(!isIntercept(session,invocation,InvocationStage.BEFORE) && postProcessor!=null){
                    postProcessor.before(invocation);
                }
            }catch (Exception e){
                isLoggingSystemException = true;
                log.error("EasylogException before the invocation of a method ",e);
            }
            result = invocation.proceed();
        }catch (Throwable throwable) {
             throughException = true;
           if(!isLoggingSystemException){
               if(!isIntercept(session,invocation,InvocationStage.EXCEPTION) && postProcessor!=null){
                   postProcessor.throwing(invocation,throwable);
               }
           }
           throw throwable;
        } finally {
            if(!isLoggingSystemException){
                if(!throughException){
                    if(!isIntercept(session,invocation,InvocationStage.AFTER) && postProcessor!=null){
                        postProcessor.after(invocation,result);
                    }
                }
            }
            if(session!=null && session.isHeadInvocation(invocation)){
                if(!session.isDestoried()){
                    session.destory(invocation);
                    for (SessionListener listener : sessionListeners) {
                        listener.destroyed(new SessionDestroyedEvent(session,invocation));
                    }
                }
                SessionHolder.clean();
            }
        }
        return result;
    }


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}

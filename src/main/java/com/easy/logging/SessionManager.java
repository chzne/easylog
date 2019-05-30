package com.easy.logging;

import com.easy.logging.session.SessionClosedEvent;

import com.easy.logging.session.SessionCreatedEvent;
import org.springframework.beans.factory.ObjectProvider;

public class SessionManager {

    protected static ThreadLocal<Session>  localSession = new ThreadLocal<>();

    private final ObjectProvider<SessionListener[]> sessionListeners;
    private final SessionFactory sessionFactory;

    public SessionManager(ObjectProvider<SessionListener[]> sessionListeners,SessionFactory sessionFactory){
        this.sessionListeners = sessionListeners;
        this.sessionFactory = sessionFactory;
    }

    public Session open(Invocation invocation){

        Session session;
        if((session = localSession.get())!=null){
            return session;
        }
        session = sessionFactory.getInstance();
        localSession.set(session);
        SessionListener[] listeners = sessionListeners.getIfAvailable();
        if(listeners!=null){
            for (SessionListener listener : listeners) {
                listener.created(new SessionCreatedEvent(session,invocation));
            }
        }
        session.start(invocation);
        return session;
    }


    public void close(Invocation invocation){
        Session session = localSession.get();
        if(session!=null && !session.isClosed()){
            session.close(invocation);
            SessionListener[] listeners = sessionListeners.getIfAvailable();
            for (SessionListener listener : listeners) {
                listener.closed(new SessionClosedEvent(session,invocation));
            }
        }
        localSession.set(null);
    }

    public static class SessionHolder{

        public static Session getSession(){

            return localSession.get();
        }

    }
}

package com.easy.logging.session;

import com.easy.logging.Invocation;
import com.easy.logging.Session;
import org.springframework.context.ApplicationEvent;

public class SessionDestroyedEvent extends ApplicationEvent {
    private final Session session;
    private final Invocation invocation;

    public SessionDestroyedEvent(Session session, Invocation invocation) {
        super(session);
        this.session = session;
        this.invocation = invocation;
    }

    public Session getSession(){

        return session;
    }

    public Invocation getInvocation(){
        return invocation;
    }
}

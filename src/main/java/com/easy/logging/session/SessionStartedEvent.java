package com.easy.logging.session;

import com.easy.logging.Invocation;
import com.easy.logging.Session;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventType;
import org.springframework.context.ApplicationEvent;

public class SessionStartedEvent extends ApplicationEvent {

    private final Session session;
    private final Invocation invocation;

    public SessionStartedEvent(Session session,Invocation invocation) {
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

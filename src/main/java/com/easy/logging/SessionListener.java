package com.easy.logging;


import com.easy.logging.session.SessionClosedEvent;
import com.easy.logging.session.SessionCreatedEvent;

public interface SessionListener {


    public void created(SessionCreatedEvent se);


    public void closed(SessionClosedEvent se);
}

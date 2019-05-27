package com.easy.logging;


import com.easy.logging.session.SessionDestroyedEvent;
import com.easy.logging.session.SessionStartedEvent;

public interface SessionListener {


    public void started(SessionStartedEvent se);


    public void destroyed(SessionDestroyedEvent se);
}

package com.easy.logging.session;

import com.easy.logging.*;

public class DistributedSessionFactory implements SessionFactory {

    @Override
    public Session getInstance(){
        DistributedSession session = new DistributedSession();
        SessionHolder.setSession(session);
        return session;
    }
}

package com.easy.logging.session;

import com.easy.logging.*;

public class InvocationSessionFactory implements SessionFactory {

    @Override
    public Session getInstance(){
        InvocationSession session = new InvocationSession();
        return session;
    }
}

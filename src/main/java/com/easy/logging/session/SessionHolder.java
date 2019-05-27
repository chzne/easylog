package com.easy.logging.session;

import com.easy.logging.Session;

public class SessionHolder {
    protected static ThreadLocal<Session> sessionThreadLocal = new ThreadLocal<>();

    static void setSession(Session session){
        sessionThreadLocal.set(session);
    }

    public static Session getSession(){
        return sessionThreadLocal.get();
    }

    public static void clean(){
        sessionThreadLocal.set(null);
    }
}

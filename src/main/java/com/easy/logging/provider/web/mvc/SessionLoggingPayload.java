package com.easy.logging.provider.web.mvc;

import com.easy.logging.LoggingPayload;
import com.easy.logging.Session;
import com.easy.logging.SessionManager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public class SessionLoggingPayload implements LoggingPayload {
    @Override
    public Map getPayload(HttpServletRequest request, HttpServletResponse response) {
        Session session = SessionManager.SessionHolder.getSession();
        Map<String, Object> map = session.getAttributes();

        return map;
    }

    @Override
    public String getName() {
        return "ThreadSession";
    }
}

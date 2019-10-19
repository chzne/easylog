package com.easy.logging;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public interface LoggingPayload {

    public Map getPayload(HttpServletRequest request, HttpServletResponse response);

    public String getName();
}

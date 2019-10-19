package com.easy.logging.provider.web.http;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface HttpRequestRecorder {

    public void record(HttpServletRequest request,HttpServletResponse response);
}

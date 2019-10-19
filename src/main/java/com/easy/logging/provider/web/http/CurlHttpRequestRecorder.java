package com.easy.logging.provider.web.http;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CurlHttpRequestRecorder implements HttpRequestRecorder{
    @Override
    public void record(HttpServletRequest request, HttpServletResponse response) {

    }
}

package com.easy.logging.monitor;

import com.alibaba.fastjson.JSON;
import com.easy.logging.*;
import com.easy.logging.provider.web.http.FilterChainInvocationAdapter;
import com.easy.logging.provider.web.mvc.MvcInvocationAdapter;
import com.easy.logging.session.SessionClosedEvent;
import com.easy.logging.session.SessionCreatedEvent;
import io.swagger.annotations.ApiOperation;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;


public class MonitorPostProcessor implements PostProcessor, SessionListener {


    protected String dingdingUrl;

    protected RestTemplate restTemplate;

    public MonitorPostProcessor(String dingdingUrl){
        this.dingdingUrl= dingdingUrl;

        RestTemplate restTemplate = new RestTemplate();
        List<HttpMessageConverter<?>> list = restTemplate.getMessageConverters();
        for (HttpMessageConverter<?> httpMessageConverter : list) {
            if(httpMessageConverter instanceof StringHttpMessageConverter) {
                ((StringHttpMessageConverter) httpMessageConverter).setDefaultCharset(Charset.forName("utf-8"));
                break;
            }
        }
        this.restTemplate = restTemplate;
    }
    @Override
    public void before(Invocation invocation) {
        if(invocation instanceof FilterChainInvocationAdapter){
            HttpServletRequest request = ((FilterChainInvocationAdapter) invocation).getHttpServletRequest();
            StringBuilder msg = new StringBuilder();
            msg.append("uri=").append(request.getRequestURI());
            String queryString = request.getQueryString();
            if (queryString != null) {
                msg.append('?').append(queryString);
            }
            SessionManager.SessionHolder.getSession().setAttribute("URL",msg.toString());
        }

        if(invocation instanceof MvcInvocationAdapter){
            ApiOperation apiOperation= invocation.getMethod().getAnnotation(ApiOperation.class);
            if(apiOperation!=null && apiOperation.value()!=null ){
                SessionManager.SessionHolder.getSession().setAttribute("ApiName",apiOperation.value());
            }
        }

    }

    protected String getMessagePayload(HttpServletRequest request) {
        ContentCachingRequestWrapper wrapper =
                WebUtils.getNativeRequest(request, ContentCachingRequestWrapper.class);
        if (wrapper != null) {
            byte[] buf = wrapper.getContentAsByteArray();
            if (buf.length > 0) {
                int length = Math.min(buf.length, 500);
                try {
                    return new String(buf, 0, length, wrapper.getCharacterEncoding());
                }
                catch (UnsupportedEncodingException ex) {
                    return "[unknown]";
                }
            }
        }
        return null;
    }

    @Override
    public void after(Invocation invocation, Object result) {
        String str = new String();
    }

    @Override
    public void throwing(Invocation invocation, Throwable throwable) {

        if(invocation instanceof FilterChainInvocationAdapter){
            HttpServletRequest request = ((FilterChainInvocationAdapter) invocation).getHttpServletRequest();
         //   String payload = getMessagePayload(request);
        //    SessionManager.SessionHolder.getSession().setAttribute("PAYLOAD",payload);
        }


        String str = new String();
    }

    @Override
    public void created(SessionCreatedEvent se) {
        String str = new String();
    }

    @Override
    public void closed(SessionClosedEvent se) {
        Session session = SessionManager.SessionHolder.getSession();

        if (session != null) {
            String traceId = session.getTrace().getTraceId();
            if (MemoryAppender.hasError(traceId)) {
                List<String> list = MemoryAppender.getLogs(traceId);
                //创建请求头
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);

                StringBuffer msg = new StringBuffer();
                msg.append("异常编号:");
                msg.append(traceId).append("\n");
              //  msg.append("接口地址:").append(session.getAttribute("URL")).append("\n");
                if(session.getAttribute("PAYLOAD")!=null){
                 //   msg.append("请求体:").append(session.getAttribute("PAYLOAD")).append("\n");
                }

                for (int i = 0; i < list.size(); i++) {
                    msg.append(list.get(i)+"\n");
                }
               // String message = "{\"msgtype\":\"text\",\"text\":{\"content\":\"" + msg.toString() + "\"}}";

                DingMessage message1 = new DingMessage();
                message1.setMsgtype("text");
                message1.setText(new Text());
                message1.getText().setContent(msg.toString());




                Object json = JSON.toJSON(message1);

                HttpEntity<String> entity = new HttpEntity<String>(json.toString(), headers);
                ResponseEntity<String> responseEntity = restTemplate.postForEntity(dingdingUrl, entity, String.class);
                String user = responseEntity.getBody();//{"msg":"调用成功！","code":1}
            }
            MemoryAppender.remove(traceId);

        }
    }

    @Data
    static class DingMessage{
        protected String msgtype;
        protected Text text;



    }

    @Data
    static class Text{
        protected String content;
    }
}

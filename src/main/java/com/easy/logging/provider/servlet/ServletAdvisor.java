package com.easy.logging.provider.servlet;

import com.easy.logging.Advisor;
import com.easy.logging.InvocationDelegator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.util.ContentCachingRequestWrapper;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
@Slf4j
public class ServletAdvisor extends OncePerRequestFilter implements HandlerInterceptor, Advisor {

    private final InvocationDelegator delegator;

    private boolean includePayload=false;

    private int maxPayloadLength=500;

    public ServletAdvisor(InvocationDelegator delegator){
        this.delegator= delegator;


    }

    public boolean isIncludePayload(){
        return includePayload;
    }

    public void setIncludePayload(boolean includePayload){
        this.includePayload = includePayload;
    }

    public void setMaxPayloadLength(int maxPayloadLength){
        this.maxPayloadLength = maxPayloadLength;
    }

    public int getMaxPayloadLength(){
        return maxPayloadLength;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        boolean isFirstRequest = !isAsyncDispatch(request);
        HttpServletRequest requestToUse = request;

        if (isIncludePayload() && isFirstRequest && !(request instanceof ContentCachingRequestWrapper)) {
            requestToUse = new ContentCachingRequestWrapper(request, getMaxPayloadLength());
        }
        ServletInvocationAdapter invocationAdapter = new ServletInvocationAdapter(requestToUse,response,filterChain );
        try {
            delegator.invoke(invocationAdapter);
        }  catch (ServletException ex) {
            throw ex;
        } catch (IOException throwable) {
            throw throwable;
        }catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }





}

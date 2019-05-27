package com.easy.logging.provider.web.http;

import com.easy.logging.InvocationProxy;
import com.easy.logging.spring.annotation.InvocationProxyAware;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;
import org.springframework.util.Assert;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExecutionChain;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.UrlPathHelper;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

final class RequestMappingFilterProxy extends OncePerRequestFilter implements   ApplicationContextAware,InvocationProxyAware {

    private static final String EXTENSION_MAPPING_PATTERN = "*.";

    private static final String PATH_MAPPING_PATTERN = "/*";

    private static final UrlPathHelper urlPathHelper = new UrlPathHelper();

    private final HttpLoggingFilter delegate;

    /** Patterns that require an exact match, e.g. "/test" */
    private final List<String> exactMatches = new ArrayList<String>();

    /** Patterns that require the URL to have a specific prefix, e.g. "/test/*" */
    private final List<String> startsWithMatches = new ArrayList<String>();

    /** Patterns that require the request URL to have a specific suffix, e.g. "*.html" */
    private final List<String> endsWithMatches = new ArrayList<String>();

    private InvocationProxy invocationProxy;
    private ApplicationContext applicationContext;
    private RequestMappingHandlerMapping requestMappingHandlerMapping;


    /**
     * Creates a new instance.
     */
    public RequestMappingFilterProxy(HttpLoggingFilter delegate) {
        Assert.notNull(delegate, "A delegate Filter is required");
        this.delegate = delegate;


    }

    @Override
    public void afterPropertiesSet() throws ServletException {
        super.afterPropertiesSet();
        Map<String, HandlerMapping> matchingBeans =
                BeanFactoryUtils.beansOfTypeIncludingAncestors(applicationContext, HandlerMapping.class, true, false);
        if (!matchingBeans.isEmpty()) {
            ArrayList<HandlerMapping> handlerMappings = new ArrayList<HandlerMapping>(matchingBeans.values());
            // We keep HandlerMappings in sorted order.
            AnnotationAwareOrderComparator.sort(handlerMappings);
            handlerMappings.forEach(mapping->{
                if(mapping instanceof RequestMappingHandlerMapping){
                    requestMappingHandlerMapping = (RequestMappingHandlerMapping) mapping;
                }
            });
        }

    }

    private void addUrlPattern(String urlPattern) {
        Assert.notNull(urlPattern, "Found null URL Pattern");
        if (urlPattern.startsWith(EXTENSION_MAPPING_PATTERN)) {
            this.endsWithMatches.add(urlPattern.substring(1, urlPattern.length()));
        }
        else if (urlPattern.equals(PATH_MAPPING_PATTERN)) {
            this.startsWithMatches.add("");
        }
        else if (urlPattern.endsWith(PATH_MAPPING_PATTERN)) {
            this.startsWithMatches.add(urlPattern.substring(0, urlPattern.length() - 1));
            this.exactMatches.add(urlPattern.substring(0, urlPattern.length() - 2));
        }
        else {
            if ("".equals(urlPattern)) {
                urlPattern = "/";
            }
            this.exactMatches.add(urlPattern);
        }
    }




    private boolean matches(String requestPath) {
        for (String pattern : this.exactMatches) {
            if (pattern.equals(requestPath)) {
                return true;
            }
        }
        if (!requestPath.startsWith("/")) {
            return false;
        }
        for (String pattern : this.endsWithMatches) {
            if (requestPath.endsWith(pattern)) {
                return true;
            }
        }
        for (String pattern : this.startsWithMatches) {
            if (requestPath.startsWith(pattern)) {
                return true;
            }
        }
        return false;
    }



    @Override
    public void destroy() {
        this.delegate.destroy();
    }


    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {

        boolean isFirstRequest = !this.isAsyncDispatch(httpServletRequest);
        HttpServletRequest requestToUse = httpServletRequest;
        if (isFirstRequest && !(httpServletRequest instanceof ContentCachingRequestWrapper)) {
            requestToUse = new ContentCachingRequestWrapper(httpServletRequest,5000);
        }
        String requestPath = urlPathHelper.getPathWithinApplication(requestToUse);
        if (isFirstRequest ) {
            HandlerExecutionChain handler = null;
            if (this.requestMappingHandlerMapping != null) {
                try {
                    handler = requestMappingHandlerMapping.getHandler(httpServletRequest);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if(handler!=null ||  matches(requestPath)){
                this.delegate.doFilter(requestToUse, httpServletResponse, filterChain);
                return;
            }
        }
        filterChain.doFilter(httpServletRequest, httpServletResponse);





    }


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void setInvocationProxy(InvocationProxy invocationProxy) {
        this.delegate.setInvocationProxy(invocationProxy);
    }
}


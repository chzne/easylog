package com.easy.logging.provider.web.http;

import com.easy.logging.Invocation;
import com.easy.logging.logging.logger.AbstractInvocationLogger;
import com.easy.logging.logging.logger.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
@Slf4j
public class HttpInvocationLogger extends AbstractInvocationLogger<FilterChainInvocationAdapter> {
    /**
     * The default value prepended to the logging message written <i>before</i> a request is
     * processed.
     */
    public static final String DEFAULT_BEFORE_MESSAGE_PREFIX = "Before request [";

    /**
     * The default value appended to the logging message written <i>before</i> a request is
     * processed.
     */
    public static final String DEFAULT_BEFORE_MESSAGE_SUFFIX = "]";

    /**
     * The default value prepended to the logging message written <i>after</i> a request is
     * processed.
     */
    public static final String DEFAULT_AFTER_MESSAGE_PREFIX = "After request [";

    /**
     * The default value appended to the logging message written <i>after</i> a request is
     * processed.
     */
    public static final String DEFAULT_AFTER_MESSAGE_SUFFIX = "]";

    private static final int DEFAULT_MAX_PAYLOAD_LENGTH = 50;

    private final static String INCLUDE_CLIENT_INFO_ATTRIBUTE_NAME = "INCLUDE_CLIENT_INFO_ATTRIBUTE_NAME";

    private final static String INCLUDE_HEADERS_ATTRIBUTE_NAME = "INCLUDE_HEADERS_ATTRIBUTE_NAME";

    private final static String INCLUDE_PAYLOAD_ATTRIBUTE_NAME = "INCLUDE_PAYLOAD_ATTRIBUTE_NAME";


    private final static String INCLUDE_ATTRIBUTE_VALUE = "YES";


    private boolean includeQueryString = false;

    private boolean includeClientInfo = false;

    private boolean includeHeaders = false;

    private boolean includePayload = false;

    private int maxPayloadLength = DEFAULT_MAX_PAYLOAD_LENGTH;

    private String beforeMessagePrefix = DEFAULT_BEFORE_MESSAGE_PREFIX;

    private String beforeMessageSuffix = DEFAULT_BEFORE_MESSAGE_SUFFIX;

    private String afterMessagePrefix = DEFAULT_AFTER_MESSAGE_PREFIX;

    private String afterMessageSuffix = DEFAULT_AFTER_MESSAGE_SUFFIX;

    /**
     * Set whether the query string should be included in the logging message.
     * <p>Should be configured using an {@code <init-param>} for parameter name
     * "includeQueryString" in the filter definition in {@code web.xml}.
     */
    public void setIncludeQueryString(boolean includeQueryString) {
        this.includeQueryString = includeQueryString;
    }


    /**
     * Set whether the client address and session id should be included in the
     * logging message.
     * <p>Should be configured using an {@code <init-param>} for parameter name
     * "includeClientInfo" in the filter definition in {@code web.xml}.
     */
    public void setIncludeClientInfo(boolean includeClientInfo) {
        this.includeClientInfo = includeClientInfo;
    }



    /**
     * Set whether the request headers should be included in the logging message.
     * <p>Should be configured using an {@code <init-param>} for parameter name
     * "includeHeaders" in the filter definition in {@code web.xml}.
     * @since 4.3
     */
    public void setIncludeHeaders(boolean includeHeaders) {
        this.includeHeaders = includeHeaders;
    }


    /**
     * Set whether the request payload (body) should be included in the logging message.
     * <p>Should be configured using an {@code <init-param>} for parameter name
     * "includePayload" in the filter definition in {@code web.xml}.
     * @since 3.0
     */
    public void setIncludePayload(boolean includePayload) {
        this.includePayload = includePayload;
    }


    /**
     * Set the maximum length of the payload body to be included in the logging message.
     * Default is 50 characters.
     * @since 3.0
     */
    public void setMaxPayloadLength(int maxPayloadLength) {
        Assert.isTrue(maxPayloadLength >= 0, "'maxPayloadLength' should be larger than or equal to 0");
        this.maxPayloadLength = maxPayloadLength;
    }

    /**
     * Return the maximum length of the payload body to be included in the logging message.
     * @since 3.0
     */
    protected int getMaxPayloadLength() {
        return this.maxPayloadLength;
    }

    /**
     * Set the value that should be prepended to the logging message written
     * <i>before</i> a request is processed.
     */
    public void setBeforeMessagePrefix(String beforeMessagePrefix) {
        this.beforeMessagePrefix = beforeMessagePrefix;
    }

    /**
     * Set the value that should be appended to the logging message written
     * <i>before</i> a request is processed.
     */
    public void setBeforeMessageSuffix(String beforeMessageSuffix) {
        this.beforeMessageSuffix = beforeMessageSuffix;
    }

    /**
     * Set the value that should be prepended to the logging message written
     * <i>after</i> a request is processed.
     */
    public void setAfterMessagePrefix(String afterMessagePrefix) {
        this.afterMessagePrefix = afterMessagePrefix;
    }

    /**
     * Set the value that should be appended to the logging message written
     * <i>after</i> a request is processed.
     */
    public void setAfterMessageSuffix(String afterMessageSuffix) {
        this.afterMessageSuffix = afterMessageSuffix;
    }

    private boolean isIncludePayload(HttpServletRequest request) {
        Object isInclude = request.getAttribute(INCLUDE_PAYLOAD_ATTRIBUTE_NAME);
        if (isInclude == null || !INCLUDE_PAYLOAD_ATTRIBUTE_NAME.equals(isInclude)) {

            return includePayload;
        }
        return true;
    }

    private boolean isIncludeHeaders(HttpServletRequest request) {
        Object isInclude = request.getAttribute(INCLUDE_HEADERS_ATTRIBUTE_NAME);
        if (isInclude == null || !INCLUDE_ATTRIBUTE_VALUE.equals(isInclude)) {

            return includeHeaders;
        }
        return true;
    }

    private boolean isIncludeClientInfo(HttpServletRequest request) {
        Object isInclude = request.getAttribute(INCLUDE_CLIENT_INFO_ATTRIBUTE_NAME);
        if (isInclude == null || !INCLUDE_CLIENT_INFO_ATTRIBUTE_NAME.equals(isInclude)) {

            return includeClientInfo;
        }
        return true;
    }

    protected String createMessage(HttpServletRequest request, String prefix, String suffix) {
        StringBuilder msg = new StringBuilder();
        msg.append(prefix);
        msg.append("uri=").append(request.getRequestURI());
        if (includeQueryString) {
            String queryString = request.getQueryString();
            if (queryString != null) {
                msg.append('?').append(queryString);
            }
        }
        getIncludeMessage(request, msg);
        msg.append(suffix);
        return msg.toString();
    }

    protected void getIncludeMessage(HttpServletRequest request, StringBuilder msg) {


        //map.put("ex", ThrowableProxyConverter.class.getName());
        //map.put("exception", ThrowableProxyConverter.class.getName());

        if (isIncludeClientInfo(request)) {
            String client = request.getRemoteAddr();
            if (StringUtils.hasLength(client)) {
                msg.append(";client=").append(client);
            }
            HttpSession session = request.getSession(false);
            if (session != null) {
                msg.append(";session=").append(session.getId());
            }
            String user = request.getRemoteUser();
            if (user != null) {
                msg.append(";user=").append(user);
            }
        }

        if (isIncludeHeaders(request)) {
            msg.append(";headers=").append(new ServletServerHttpRequest(request).getHeaders());
        }

        if (isIncludePayload(request)) {
            String payload = getMessagePayload(request);
            if (payload != null) {
                msg.append(";payload=").append(payload);
            }
        }

    }

    protected String getMessagePayload(HttpServletRequest request) {
        ContentCachingRequestWrapper wrapper =
                WebUtils.getNativeRequest(request, ContentCachingRequestWrapper.class);
        if (wrapper != null) {
            byte[] buf = wrapper.getContentAsByteArray();
            if (buf.length > 0) {
                int length = Math.min(buf.length, getMaxPayloadLength());
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
    public Message beforeMessage(FilterChainInvocationAdapter invocation) {
        String message = createMessage(invocation.getHttpServletRequest(), this.beforeMessagePrefix, this.beforeMessageSuffix);

        return new Message("{}",message);
    }

    @Override
    public Message afterMessage(FilterChainInvocationAdapter invocation, Object result) {
        String message = createMessage(invocation.getHttpServletRequest(), this.afterMessagePrefix, this.afterMessageSuffix);
        return new Message("{}",message);
    }




    @Override
    public int getPriority(Class<? extends Invocation> invocation) {
        if(invocation.getName().equalsIgnoreCase(HttpInvocationLogger.class.getName())){
            return 8;
        }
        return -1;
    }

    @Override
    public Class<Invocation>[] getSupportedInvocationType() {
        return new Class[]{FilterChainInvocationAdapter.class};
    }

    @Override
    public void throwing(FilterChainInvocationAdapter invocation, Throwable throwable) {

    }
}

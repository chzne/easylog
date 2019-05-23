package com.easy.logging.logging.advice;

import com.easy.logging.Advice;
import com.easy.logging.Invocation;
import com.easy.logging.InvocationLogger;
import com.easy.logging.LoggerRegistry;
import org.springframework.core.Ordered;

import java.util.List;

public class LoggingAdvice implements Advice, Ordered {

    protected final static String INVOCATION_LOGGER_ATTRIBUTE_NAME="invocation_logger_attribute_name";

    private final LoggerRegistry loggerRegistry;

    public LoggingAdvice(LoggerRegistry loggerRegistry){
        this.loggerRegistry = loggerRegistry;
    }

    @Override
    public void before(Invocation invocation) {
        InvocationLogger logger = loggerRegistry.getLoggerWithHighPriority(invocation.getClass());
        invocation.setAttribute(INVOCATION_LOGGER_ATTRIBUTE_NAME,logger);
        logger.before(invocation);
    }

    @Override
    public void after(Invocation invocation, Object result) {
        InvocationLogger logger = (InvocationLogger) invocation.getAttribute(INVOCATION_LOGGER_ATTRIBUTE_NAME);
        logger.after(invocation,result);
    }

    @Override
    public void throwing(Invocation invocation, Throwable throwable) {
        InvocationLogger logger = (InvocationLogger) invocation.getAttribute(INVOCATION_LOGGER_ATTRIBUTE_NAME);
        logger.throwing(invocation,throwable);
    }

    /**
     * Get the order value of this object.
     * <p>Higher values are interpreted as lower priority. As a consequence,
     * the object with the lowest value has the highest priority (somewhat
     * analogous to Servlet {@code load-on-startup} values).
     * <p>Same order values will result in arbitrary sort positions for the
     * affected objects.
     *
     * @return the order value
     * @see #HIGHEST_PRECEDENCE
     * @see #LOWEST_PRECEDENCE
     */
    @Override
    public int getOrder() {
        return 0;
    }
}

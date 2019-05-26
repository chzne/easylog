package com.easy.logging.logging.processor;

import com.easy.logging.*;
import com.easy.logging.PostProcessor;
import org.springframework.core.Ordered;

import java.util.List;

public class LoggingPostProcessor implements PostProcessor, Ordered {

    protected final static String INVOCATION_LOGGER_ATTRIBUTE_NAME="invocation_logger_attribute_name";

    private final LoggerSelector loggerSelector;

    public LoggingPostProcessor(LoggerSelector loggerSelector){
        this.loggerSelector = loggerSelector;
    }

    @Override
    public void before(Invocation invocation) {
        List<InvocationLogger> loggers = loggerSelector.select(invocation);
        loggers.forEach(logger->logger.before(invocation));

    }

    @Override
    public void after(Invocation invocation, Object result) {
        List<InvocationLogger> loggers = loggerSelector.select(invocation);
        loggers.forEach(logger->logger.after(invocation,result));
    }

    @Override
    public void throwing(Invocation invocation, Throwable throwable) {
        List<InvocationLogger> loggers = loggerSelector.select(invocation);
        loggers.forEach(logger->logger.throwing(invocation,throwable));
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

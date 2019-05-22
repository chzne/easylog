package com.easy.logging.logging.advice;

import com.easy.logging.Advice;
import com.easy.logging.LoggerRegistry;
import com.easy.logging.logging.logger.CompositeInvocationLogger;
import com.easy.logging.Invocation;
import org.springframework.core.Ordered;

public class LoggingAdvice implements Advice, Ordered {

    private final LoggerRegistry loggerRegistry;

    public LoggingAdvice(LoggerRegistry loggerRegistry){
        this.loggerRegistry = loggerRegistry;
    }

    @Override
    public void before(Invocation invocation) {

        CompositeInvocationLogger logger = loggerRegistry.getCompositeLogger(invocation.getClass());
        logger.before(invocation);

    }

    @Override
    public void after(Invocation invocation, Object result) {
        CompositeInvocationLogger logger = loggerRegistry.getCompositeLogger(invocation.getClass());
        logger.after(invocation,result);
    }

    @Override
    public void throwing(Invocation invocation, Throwable throwable) {

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

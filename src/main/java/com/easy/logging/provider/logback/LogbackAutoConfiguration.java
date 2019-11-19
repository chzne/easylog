package com.easy.logging.provider.logback;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.PatternLayout;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import ch.qos.logback.core.OutputStreamAppender;
import ch.qos.logback.core.encoder.LayoutWrappingEncoder;
import com.easy.logging.Trace;
import com.easy.logging.monitor.MemoryAppender;
import org.slf4j.ILoggerFactory;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Configuration
@ConditionalOnClass(ch.qos.logback.classic.spi.ILoggingEvent.class)
public class LogbackAutoConfiguration   {

    @PostConstruct
    public void logbackTraceInjection(){
        ILoggerFactory iLoggerFactory = LoggerFactory.getILoggerFactory();
        if (iLoggerFactory instanceof LoggerContext) {
            LoggerContext loggerContext = (LoggerContext) iLoggerFactory;

            Logger logger = loggerContext.getLogger("ROOT");

            Iterator<Appender<ILoggingEvent>> appenders = logger.iteratorForAppenders();
            while (appenders.hasNext()) {
                Appender<ILoggingEvent> appender =  appenders.next();
                if(OutputStreamAppender.class.isAssignableFrom(appender.getClass())){
                    LayoutWrappingEncoder encoder = (LayoutWrappingEncoder) ((OutputStreamAppender<ILoggingEvent>) appender).getEncoder();
                    PatternLayout layout = (PatternLayout) encoder.getLayout();
                    String Layoutpattern = layout.getPattern();
                    Pattern pattern = Pattern.compile(".*(logger\\{[0-9]{1,3}\\}).*");
                    Matcher matcher = pattern.matcher(Layoutpattern);
                    boolean matches = matcher.matches();
                    if(matches){
                        String group = matcher.group(1);
                        Layoutpattern = Layoutpattern.replace(group,group+" %X{"+ Trace.DEFAULT_TRACE_MDC_PARAMETER_NAME+"}");
                    }
                    layout.setPattern(Layoutpattern);
                    layout.stop();
                    layout.setPostCompileProcessor(new TraceEnsureExceptionHandling());
                    layout.start();
                }
            }

            logger.addAppender(new MemoryAppender());


        }
    }

}

package com.easy.logging.provider.logback;

import ch.qos.logback.classic.PatternLayout;

public class TracePatternLayout extends PatternLayout {

    public TracePatternLayout() {
        this.postCompileProcessor = new TraceEnsureExceptionHandling();
    }
}

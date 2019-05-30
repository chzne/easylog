package com.easy.logging.logging.config;

import lombok.Data;

@Data
public class InvocationLoggingConfig {

    private boolean includeArguments = true;

    private boolean includeResult = true;

    private boolean includeException = true;

    private boolean includeElapsedTime = true;

    private boolean includeLoggingAnnotationValue = true;

    private String beforePrefix="【method argurments】";

    private String beforeSuffix="";

    private String afterPrefix="【returning result】";

    private String afterSuffix="";

    private String exceptionPrefix="【exception message】";

    private String exceptionSuffix="";

    private String exceptionClassPrefix="【exception class】";

    private String exceptionMessagePrefix="【exception message】";

    private String exceptionLocationPrefix="【exception location】";

    private String elapsedTimePrefix="【time:";

    private String elapsedTimeSuffix="】";
}

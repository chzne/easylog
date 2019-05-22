package com.easy.logging.spring.annotation;

import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({EasylogAspectRegistrar.class, LoggingConfigurationSelector.class})
@EnableAspectJAutoProxy
public @interface EnableEasylogAspect {
}

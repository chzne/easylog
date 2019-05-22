package com.easy.logging.spring.annotation;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface Logging {


    @AliasFor("prefix")
    String value() default "";

    @AliasFor("value")
    String prefix() default "";
    /**只对Controller有效 输出HTTP信息*/
    Request[] include() default {};


}

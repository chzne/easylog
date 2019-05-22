package com.easy.logging.spring.annotation;


import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

public class LoggingConfigurationSelector implements ImportSelector {

    @Override
    public String[] selectImports(AnnotationMetadata annotationMetadata) {

        return new String[]{EasyLogWebMvcConfiguration.class.getName()};
    }
}

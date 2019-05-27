package com.easy.logging.spring.annotation;


import com.easy.logging.provider.mybatis.MybatisLoggingConfiguration;
import com.easy.logging.spring.EasylogConfiguration;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

public class LoggingConfigurationSelector implements ImportSelector {

    @Override
    public String[] selectImports(AnnotationMetadata annotationMetadata) {

        return new String[]{EasylogConfiguration.class.getName(), MybatisLoggingConfiguration.class.getName()};
    }
}
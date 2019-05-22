package com.easy.logging.spring.autoconfigure;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "easylog")
public class EasylogProperties {

    protected String[] repositoryPackage;
}

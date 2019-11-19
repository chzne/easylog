package com.easy.logging.spring.autoconfigure;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
@Data
@ConfigurationProperties(prefix = "easylog")
public class EasylogProperties {

    protected String[] repositoryPackage;

    protected String dingdingUrl;
}

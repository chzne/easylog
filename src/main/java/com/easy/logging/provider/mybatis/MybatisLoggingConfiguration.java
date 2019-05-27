package com.easy.logging.provider.mybatis;


import org.apache.ibatis.session.SqlSessionFactory;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;

import javax.annotation.PostConstruct;


@ConditionalOnBean(SqlSessionFactory.class)
@AutoConfigureAfter(name = {"org.mybatis.spring.boot.autoconfigure.MybatisAutoConfiguration"})
public class MybatisLoggingConfiguration{

    private final SqlSessionFactory sqlSessionFactory;

    public MybatisLoggingConfiguration(SqlSessionFactory sqlSessionFactory){
        this.sqlSessionFactory = sqlSessionFactory;
    }

    @PostConstruct
    public void addMybatisLoggingInterceptor() {
        MybatisLoggingInterceptor interceptor = new MybatisLoggingInterceptor();
        sqlSessionFactory.getConfiguration().addInterceptor(interceptor);

    }



}

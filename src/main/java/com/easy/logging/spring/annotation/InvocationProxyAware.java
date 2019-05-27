package com.easy.logging.spring.annotation;

import com.easy.logging.InvocationProxy;
import org.springframework.beans.factory.Aware;

public interface InvocationProxyAware extends Aware {

    void setInvocationProxy(InvocationProxy invocationProxy);

}

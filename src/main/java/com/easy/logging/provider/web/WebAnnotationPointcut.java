package com.easy.logging.provider.web;

import com.easy.logging.invocation.delegator.DefaultInvocationDelegator;
import org.springframework.aop.support.StaticMethodMatcherPointcut;
import org.springframework.core.BridgeMethodResolver;
import org.springframework.util.ClassUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;

public class WebAnnotationPointcut extends StaticMethodMatcherPointcut {

    protected List<Class<? extends Annotation>> targetAnnotations = new LinkedList<>();

    protected List<Class<? extends Annotation>> methodAnnotations = new LinkedList<>();

    public void addTargetAnnotation(Class<? extends Annotation> annotationClass) {
        targetAnnotations.add(annotationClass);
    }

    public void addMethodAnnotation(Class<? extends Annotation> annotationClass) {
        methodAnnotations.add(annotationClass);
    }


    @Override
    public boolean matches(Method method, Class<?> targetClass) {
        if (targetClass != null && DefaultInvocationDelegator.class.isAssignableFrom(targetClass)) {
            return false;
        } else {
            Class<?> userClass = ClassUtils.getUserClass(targetClass);

            Method specificMethod = ClassUtils.getMostSpecificMethod(method, userClass);
            specificMethod = BridgeMethodResolver.findBridgedMethod(specificMethod);


            for (int j = 0; j < targetAnnotations.size(); j++) {
                Annotation targetAnnotation = specificMethod.getDeclaringClass().getAnnotation(targetAnnotations.get(j));
                if (targetAnnotation != null && ClassUtils.isUserLevelMethod(method)) {
                    return true;
                } else {
                    if (specificMethod != method) {
                        targetAnnotation = method.getDeclaringClass().getAnnotation(targetAnnotations.get(j));
                        if (targetAnnotation != null && ClassUtils.isUserLevelMethod(method)) {
                            return true;
                        }
                    }

                }
            }

            for (int i = 0; i < methodAnnotations.size(); i++) {
                Annotation annotation = specificMethod.getAnnotation(methodAnnotations.get(i));
                if(annotation!=null){
                    return true;
                }else{

                    if (specificMethod != method) {
                        annotation = method.getAnnotation(methodAnnotations.get(i));
                        if (annotation != null) {
                            return true;
                        }
                    }

                }
            }


            return false;
        }
    }
}

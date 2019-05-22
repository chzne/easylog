package com.easy.logging.provider.spring;

import com.easy.logging.invocation.delegator.AdviceInvocationDelegator;
import org.springframework.aop.support.StaticMethodMatcherPointcut;
import org.springframework.core.BridgeMethodResolver;
import org.springframework.util.ClassUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;

public class AnnotationPointcut extends StaticMethodMatcherPointcut {

    protected List<Class<? extends Annotation>> targetAnnotations = new LinkedList<>();

    protected List<Class<? extends Annotation>> methodAnnotations = new LinkedList<>();

    protected List<String> packageNames = new LinkedList<>();




    public void addPackageName(String packageName) {
        packageNames.add(packageName);
    }

    public void addTargetAnnotation(Class<? extends Annotation> annotationClass) {
        targetAnnotations.add(annotationClass);
    }

    public void addMethodAnnotation(Class<? extends Annotation> annotationClass) {
        methodAnnotations.add(annotationClass);
    }


    @Override
    public boolean matches(Method method, Class<?> targetClass) {
        if (targetClass != null && AdviceInvocationDelegator.class.isAssignableFrom(targetClass)) {
            return false;
        } else {
            Class<?> userClass = ClassUtils.getUserClass(targetClass);

            Method specificMethod = ClassUtils.getMostSpecificMethod(method, userClass);
            specificMethod = BridgeMethodResolver.findBridgedMethod(specificMethod);

            if(targetClass.getName().contains("proxy")){
                Class<?> clacss = method.getDeclaringClass();

                if(clacss!=null && clacss.getPackage()!=null &&  packageNames.contains(clacss.getPackage().getName())){
                    return true;
                }
                return false;
            }


            /*if(packageNames.contains(specificMethod.getDeclaringClass().getName())){
                return true;
            }*/
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
                            return false;
                        }
                    }
                }
            }
            return false;
        }
    }
}

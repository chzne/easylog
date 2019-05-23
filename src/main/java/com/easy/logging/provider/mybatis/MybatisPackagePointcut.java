package com.easy.logging.provider.mybatis;

import com.easy.logging.invocation.delegator.DefaultInvocationDelegator;
import org.springframework.aop.support.StaticMethodMatcherPointcut;
import org.springframework.core.BridgeMethodResolver;
import org.springframework.util.ClassUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;

public class MybatisPackagePointcut extends StaticMethodMatcherPointcut {


    protected List<String> packageNames = new LinkedList<>();

    public void addPackageName(String packageName) {
        packageNames.add(packageName);
    }

    @Override
    public boolean matches(Method method, Class<?> targetClass) {
        if (targetClass != null && DefaultInvocationDelegator.class.isAssignableFrom(targetClass)) {
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
            return false;
        }
    }
}

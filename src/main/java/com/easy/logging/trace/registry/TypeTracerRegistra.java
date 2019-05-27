package com.easy.logging.trace.registry;

import com.easy.logging.Invocation;
import com.easy.logging.Tracer;
import com.easy.logging.TracerRegistra;
import com.easy.logging.TracerRegistry;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;

public class TypeTracerRegistra implements TracerRegistra {

    @Override
    public void addTracer(TracerRegistry registry,Tracer tracer) {
        Class<? extends Tracer> clazz = tracer.getClass();
        Type[] types = clazz.getGenericInterfaces();
        Type type = types[0];
        if(ParameterizedType.class.isAssignableFrom(type.getClass())){
            ParameterizedType parameterizedType = (ParameterizedType) type;
            Type actualType = parameterizedType.getActualTypeArguments()[0];
            if(TypeVariable.class.isAssignableFrom(actualType.getClass())){
                TypeVariable typeVariable = (TypeVariable) actualType;
                Class<? extends Invocation> invocationType = (Class<? extends Invocation>) typeVariable.getBounds()[0];
                registry.register(invocationType,tracer);
            }else{
                Class<? extends Invocation> invocationType = (Class<? extends Invocation>) actualType;
                registry.register(invocationType,tracer);
            }
        }

    }
}

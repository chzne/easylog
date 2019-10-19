package com.easy.logging.invocation.advice;

import com.easy.logging.EasylogSystemException;
import com.easy.logging.Invocation;
import com.easy.logging.PostProcessor;
import org.springframework.beans.factory.ObjectProvider;

import java.util.Arrays;

public class OrderedCompositePostProcessor implements PostProcessor {



    private final ObjectProvider<PostProcessor[]> provider;
    private PostProcessor[] postProcessors;

    public OrderedCompositePostProcessor(ObjectProvider<PostProcessor[]> provider){

        this.provider = provider;

    }


    /**
     * get called before invocation of a method
     *
     * @param invocation
     */
    @Override
    public void before(Invocation invocation) {
        if(postProcessors==null){
            postProcessors = this.provider.getIfAvailable();
        }

        Arrays.stream(postProcessors).forEach(a ->a.before(invocation));

    }

    /**
     * get called after invocation of a method
     *
     * @param invocation
     * @param result
     */
    @Override
    public void after(Invocation invocation, Object result) {
        PostProcessor[] postProcessors = this.provider.getIfAvailable();
        try{
            Arrays.stream(postProcessors).forEach(a ->a.after(invocation,result));
        }catch (Exception e){
            throw e;
        }

    }

    /**
     * get called when exception occurred in a invocation
     *
     * @param invocation
     * @param throwable
     */
    @Override
    public void throwing(Invocation invocation, Throwable throwable) {
        PostProcessor[] postProcessors = this.provider.getIfAvailable();
        try{
            Arrays.stream(postProcessors).forEach(a ->a.throwing(invocation,throwable));
        }catch (Exception e){
            throw new EasylogSystemException();
        }

    }
}

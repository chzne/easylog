package com.easy.logging;

public class ExceptionHolder {

    protected Throwable throwable;

    private static final ThreadLocal<ExceptionHolder> local = new ThreadLocal<ExceptionHolder>() {
        @Override
        protected ExceptionHolder initialValue() {

            return new ExceptionHolder();
        }
    };

    public static ExceptionHolder currentExceptionHolder(){

        return local.get();
    }


    public void setException(Throwable e) {
        this.throwable= e;
    }


    public boolean hasException() {
        return this.throwable==null?false:true;
    }

}

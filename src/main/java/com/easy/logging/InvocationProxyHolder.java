package com.easy.logging;

public class InvocationProxyHolder   {


    private  static InvocationProxy invocationProxy;


    public static void setInvocationProxy(InvocationProxy proxy) {
        invocationProxy = proxy;
    }

    public static InvocationProxy getInvocationProxy(){
        return invocationProxy;
    }
}

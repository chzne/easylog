package com.easy.logging.logging.logger;

public class Message {

    public Message( String format,Object message) {
        this.message = message;
        this.format = format;
    }

    protected Object message;

    protected String format;

    public Object getMessage() {
        return message;
    }



    public String getFormat() {
        return format;
    }


}

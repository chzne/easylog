package com.easy.logging.logging.logger.wrapper;

import org.slf4j.Logger;

public class LoggerWrapper {

    private final Logger logger;

    public LoggerWrapper(Logger logger){
        this.logger = logger;
    }

    public void info(String format,Object... objects){

        for (int i = 0; i < objects.length; i++) {
           // if(objects[i] instanceof )
        }

        logger.info(format,objects);
    }
}

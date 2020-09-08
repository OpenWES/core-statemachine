package com.openwes.test;


import com.openwes.core.annotation.Implementation;
import com.openwes.statemachine.ActorProps;
import com.openwes.statemachine.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author xuanloc0511@gmail.com
 */
@Implementation
public class ProcessActionAny2D extends Processor<Object>{

    private final static Logger LOGGER = LoggerFactory.getLogger(ProcessActionAny2D.class);

    @Override
    public boolean onProcess(ActorProps props, Object data) {
        LOGGER.info("on Process {} {}", props, data);
        try {
            Thread.sleep(200);
        } catch (InterruptedException ex) {
        }
        return true;
    }

}

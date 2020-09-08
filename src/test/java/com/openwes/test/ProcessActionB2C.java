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
public class ProcessActionB2C extends Processor<Object> {

    private final static Logger LOGGER = LoggerFactory.getLogger(ProcessActionB2C.class);

    @Override
    public boolean onProcess(ActorProps props, Object data) {
        LOGGER.info("on Process {} {}", props, data);
        trigger("ACTION_3", data);
        return true;
    }

}

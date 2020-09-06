package com.openwes.test;


import com.openwes.core.annotation.Implementation;
import com.openwes.workflow.ActorProps;
import com.openwes.workflow.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author xuanloc0511@gmail.com
 */
@Implementation
public class ProcessActionA2B extends Processor<Object> {

    private final static Logger LOGGER = LoggerFactory.getLogger(ProcessActionA2B.class);

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

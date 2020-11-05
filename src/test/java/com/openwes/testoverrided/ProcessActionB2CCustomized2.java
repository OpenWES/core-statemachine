package com.openwes.testoverrided;

import com.openwes.core.annotation.Implementation;
import com.openwes.statemachine.ActorProps;
import com.openwes.statemachine.Processor;
import com.openwes.test.ProcessActionB2CCustomized;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author xuanloc0511@gmail.com
 */
@Implementation(of = ProcessActionB2CCustomized.class)
public class ProcessActionB2CCustomized2 extends Processor<Object> {

    private final static Logger LOGGER = LoggerFactory.getLogger(ProcessActionB2CCustomized2.class);

    @Override
    public boolean onProcess(ActorProps props, Object data) {
        LOGGER.info("on Process {} {}", props, data);
        return true;
    }

}

package com.openwes.workflow;

import com.openwes.workflow.utils.ClassUtils;
import com.openwes.workflow.utils.ClockService;
import com.openwes.workflow.utils.ClockWatch;
import java.util.concurrent.LinkedBlockingQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author xuanloc0511@gmail.com
 */
public class Worker extends Thread {

    private final static Logger LOGGER = LoggerFactory.getLogger(Worker.class);

    private final LinkedBlockingQueue<Command> context = new LinkedBlockingQueue<>();

    @Override
    public void run() {
        while (!isInterrupted()) {
            Command cmd = null;
            try {
                cmd = context.take();
                if (cmd == null) {
                    break;
                }
                ClockWatch cw = ClockService.newClockWatch();
                Processor processor = ClassUtils.object(cmd.getProcessor());
                processor.onProcess(cmd.getActorId(), cmd.getProps(), cmd.getData());
                cw.timeElapsedMS();
                cmd.complete();
            } catch (Exception ex) {
                LOGGER.error("");
                if(cmd != null){
                    cmd.error(ex);
                }
            } 
        }
    }

    public void terminate() {
        interrupt();
    }
}

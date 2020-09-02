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

    private final LinkedBlockingQueue<Command> commands = new LinkedBlockingQueue<>();

    void sendCommand(Command cmd) throws InterruptedException {
        commands.put(cmd);
    }

    @Override
    public void run() {
        while (!isInterrupted()) {
            Command cmd = null;
            try {
                cmd = commands.take();
                if (cmd == null) {
                    break;
                }
                if (cmd instanceof Killed) {
                    break;
                }
                ClockWatch cw = ClockService.newClockWatch(cmd.getStarted());
                LOGGER.info("preparing time: {}", cw.timeElapsedMS());
                Processor processor = ClassUtils.object(cmd.getProcessor());

                //update process attribute
                processor.setActorId(cmd.getActorId());
                processor.setActorType(cmd.getActorType());
                processor.setActionId(cmd.getActionId());
                //process action
                if (processor.onProcess(cmd.getProps(), cmd.getData())) {
                    cmd.complete();
                } else {
                    cmd.fail();
                }
                LOGGER.info("processing time: {}", cw.timeElapsedMS());
            } catch (Exception ex) {
                LOGGER.error("handle command get error", ex);
                if (cmd != null) {
                    cmd.error(ex);
                }
            }
        }
    }

    public void terminate() {
        try {
            sendCommand(new Killed());
        } catch (InterruptedException ex) {
        }
        interrupt();
    }
}

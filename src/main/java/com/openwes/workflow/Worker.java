package com.openwes.workflow;

import static com.openwes.core.IOC.init;
import com.openwes.core.Transaction;
import com.openwes.core.logging.LogContext;
import com.openwes.core.utils.ClassUtils;
import com.openwes.core.utils.ClockService;
import com.openwes.core.utils.ClockWatch;
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

    private final Transaction transaction = init(Transaction.class);
    private final int index;

    public Worker(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

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
                LogContext.set(LogContext.TXID, cmd.getTxId());
                LOGGER.info("preparing time: {}", cw.timeElapsedMS());
                Processor processor = ClassUtils.object(cmd.getProcessor());

                //update process attribute
                processor.setActorId(cmd.getActorId());
                processor.setActorType(cmd.getActorType());
                processor.setActionId(cmd.getActionId());
                //process action
                transaction.begin();
                if (processor.onProcess(cmd.getProps(), cmd.getData())) {
                    cmd.complete();
                } else {
                    cmd.fail();
                }
                transaction.commit();
                LOGGER.info("processing time: {}", cw.timeElapsedMS());
            } catch (Exception ex) {
                try {
                    LOGGER.error("handle command get error", ex);
                    if (cmd != null) {
                        cmd.error(ex);
                    }
                    transaction.rollback();
                } catch (Exception e) {
                    LOGGER.error("rollback transaction get error", ex);
                }
            } finally {
                try {
                    transaction.end();
                } catch (Exception ex) {
                    LOGGER.error("end transaction get error", ex);
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

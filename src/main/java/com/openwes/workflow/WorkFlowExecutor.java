package com.openwes.workflow;

/**
 *
 * @author xuanloc0511@gmail.com
 * @since Sep 1, 2020
 * @version 1.0.0
 *
 */
class WorkFlowExecutor {

    private final Worker w = new Worker();

    void start() {
        w.start();
    }

    public void submit(Command cmd) throws InterruptedException {
        w.sendCommand(cmd);
    }

    public void shutdown() {
        w.terminate();
    }
}

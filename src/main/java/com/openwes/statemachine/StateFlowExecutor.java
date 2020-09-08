package com.openwes.statemachine;

import com.openwes.core.utils.FNVHash;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author xuanloc0511@gmail.com
 * @since Sep 1, 2020
 * @version 1.0.0
 *
 */
class StateFlowExecutor {

    private final List<Worker> workers = new ArrayList();

    void start(int maxWorkerSize) {
        if (maxWorkerSize == 0) {
            throw new RuntimeException("can not set worker size smaller than 1");
        }
        for (int i = 0; i < maxWorkerSize; i++) {
            Worker w = new Worker(i + 1);
            w.setDaemon(true);
            w.setName(String.format("WorkflowWorker-%d", i + 1));
            w.start();
            workers.add(w);
        }
    }

    public void submit(Command cmd) throws InterruptedException {
        if (workers.isEmpty()) {
            return;
        }
        Worker w = workers.get(Math.abs(FNVHash.hash32(cmd.getActorId()) % workers.size()));
        if (w == null) {
            return;
        }
        w.sendCommand(cmd);
    }

    public void shutdown() {
        workers.forEach((w) -> {
            w.terminate();
        });
        workers.clear();
    }
}

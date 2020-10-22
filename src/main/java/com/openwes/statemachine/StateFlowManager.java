package com.openwes.statemachine;

import com.openwes.core.IOC;
import com.openwes.core.utils.ClassLoadException;
import com.openwes.core.utils.ClassUtils;
import com.openwes.core.utils.Validate;
import com.typesafe.config.Config;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author xuanloc0511@gmail.com
 * @since Sep 1, 2020
 * @version 1.0.0
 *
 */
public class StateFlowManager {

    private final static StateFlowManager INSTANCE = new StateFlowManager();

    public final static StateFlowManager instance() {
        return INSTANCE;
    }

    public final static StateFlow workflow(String actorType) {
        return StateFlowManager.instance()
                .findWorkFlow(actorType);
    }

    private final Map<String, StateFlow> workflows = new HashMap<>();
    private final StateFlowExecutor executor = new StateFlowExecutor();

    private StateFlowManager() {

    }

    StateFlowExecutor getExecutor() {
        return executor;
    }

    public StateFlow findWorkFlow(String actorType) {
        if (Validate.isEmpty(actorType)) {
            throw new RuntimeException("");
        }
        return workflows.get(actorType);
    }

    public StateFlowManager register(StateFlow wf) {
        if (wf == null) {
            throw new RuntimeException("");
        }
        StateFlow old = findWorkFlow(wf.getType());
        if (old != null) {
            old.getTransitions().forEach((transition) -> {
                wf.addTransition(transition);
            });
        }
        workflows.put(wf.getType(), wf);
        return this;
    }

    public StateFlowManager unregister(String actorType) {
        StateFlow wf = findWorkFlow(actorType);
        if (wf == null) {
            return this;
        }
        wf.shutdown();
        workflows.remove(wf.getType());
        return this;
    }

    public void start(Config config) throws ClassLoadException {
        int workerSize = config.getInt("worker-size");
        if (workerSize < 1) {
            throw new RuntimeException("worker size must larger than 0");
        }
        ActionQueueFactory queueFactory = IOC.init(ActionQueueFactory.class);
        //setup action queue factory
        ActionManager.instance().setActionQueueFactory(queueFactory);

        //setup worker size
        workerSize = Math.max(Runtime.getRuntime().availableProcessors(), workerSize);
        executor.start(workerSize);
    }

    public void shutdown() {
        executor.shutdown();
        workflows.forEach((actorType, workFlow) -> {
            workFlow.shutdown();
        });
        workflows.clear();
    }
}

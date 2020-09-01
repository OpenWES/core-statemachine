package com.openwes.workflow;

import com.openwes.workflow.utils.Validate;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author xuanloc0511@gmail.com
 * @since Sep 1, 2020
 * @version 1.0.0
 *
 */
public class WorkFlowManager {

    private final static WorkFlowManager INSTANCE = new WorkFlowManager();

    public final static WorkFlowManager instance() {
        return INSTANCE;
    }

    private final Map<String, WorkFlow> workflows = new HashMap<>();
    private final WorkFlowExecutor executor = new WorkFlowExecutor();

    private WorkFlowManager() {

    }

    WorkFlowExecutor getExecutor() {
        return executor;
    }

    public WorkFlow workFlow(String actorType) {
        if (Validate.isEmpty(actorType)) {
            throw new RuntimeException("");
        }
        return workflows.get(actorType);
    }

    public WorkFlowManager register(WorkFlow wf) {
        if (wf == null) {
            throw new RuntimeException("");
        }
        WorkFlow old = workFlow(wf.getType());
        if (old != null) {
            old.getTransitions().forEach((transition) -> {
                wf.addTransition(transition);
            });
        }
        workflows.put(wf.getType(), wf);
        return this;
    }

    public WorkFlowManager unregister(String actorType) {
        WorkFlow wf = workFlow(actorType);
        if (wf == null) {
            return this;
        }
        wf.shutdown();
        workflows.remove(wf.getType());
        return this;
    }

    public void shutdown() {
        workflows.forEach((actorType, workFlow) -> {
            workFlow.shutdown();
        });
        workflows.clear();
    }
}

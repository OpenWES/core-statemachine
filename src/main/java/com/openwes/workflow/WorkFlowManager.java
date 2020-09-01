package com.openwes.workflow;

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

    public <T extends Actor> WorkFlow workFlow(Class<T> clzz) {
        if (clzz == null) {
            throw new RuntimeException("");
        }
        return workflows.get(clzz.getName());
    }

    public WorkFlowManager register(WorkFlow wf) {
        if (wf == null) {
            throw new RuntimeException("");
        }
        workflows.put(wf.getType(), wf);
        return this;
    }

    public <T extends Actor> WorkFlowManager unregister(Class<T> clzz) {
        WorkFlow wf = workFlow(clzz);
        if (wf == null) {
            return this;
        }
        wf.shutdown();
        workflows.remove(wf.getType());
        return this;
    }
}

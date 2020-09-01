package com.openwes.workflow;

import java.util.PriorityQueue;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 *
 * @author xuanloc0511@gmail.com
 * @since Sep 1, 2020
 * @version 1.0.0
 *
 */
public class Actor {

    private final AtomicBoolean inProcess = new AtomicBoolean(false);
    private final PriorityQueue<Action> actions = new PriorityQueue<>((left, right) -> {
        return (int) (left.getId() - right.getId());
    });
    private final Object mutex = new Object();
    private String id;
    private String currentState;
    private TransitionLookup lookup;

    TransitionLookup getLookup() {
        return lookup;
    }

    Actor setLookup(TransitionLookup lookup) {
        this.lookup = lookup;
        return this;
    }

    public String getId() {
        return id;
    }

    public String getCurrentState() {
        return currentState;
    }

    void enqueueAction(Action action) {
        actions.add(action);
    }

    void nextAction() {
        synchronized (mutex) {
            if (inProcess.compareAndSet(true, true)) {
                //it is being processed
                return;
            }
            Action action = dequeueAction();
            if (action == null) {
                inProcess.set(false);
                return;
            }
            Transition transition = lookup.lookup(currentState, action.getName());
            if (transition == null) {
                inProcess.set(false);
                nextAction();
                return;
            }
            inProcess.set(true);
            WorkFlowManager.instance()
                    .getExecutor()
                    .submit();
        }
    }

    Action dequeueAction() {
        return actions.poll();
    }

    void clearAction() {
        actions.clear();
    }

    public int remainingAction() {
        return actions.size();
    }
}

package com.openwes.statemachine;

import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

/**
 *
 * @author xuanloc0511@gmail.com
 */
class InternalActionQueue<E extends Action> implements ActionQueueFactory<E> {

    private final Map<String, PriorityQueue<E>> actions = new HashMap<>();

    public InternalActionQueue() {
    }

    @Override
    public void createQueue(String actorId) {
        if (actions.containsKey(actorId)) {
            return;
        }
        actions.put(actorId, new PriorityQueue<>((left, right) -> {
            //sort by timestamp if they are same id
            if (left.getId() == right.getId()) {
                return (int) (left.getCreated() - right.getCreated());
            }
            return (int) (left.getId() - right.getId());
        }));
    }

    @Override
    public void enqueueAction(String actorId, E action) {
        PriorityQueue actionQueue = actions.get(actorId);
        if (actionQueue == null) {
            throw new RuntimeException(new StringBuilder()
                    .append("Action queue of actor ")
                    .append(actorId)
                    .append(" is not created")
                    .toString());
        }
        actionQueue.add(action);
    }

    @Override
    public E dequeueAction(String actorId) {
        PriorityQueue<E> actionQueue = actions.get(actorId);
        if (actionQueue == null) {
            throw new RuntimeException("");
        }
        return actionQueue.poll();
    }

    @Override
    public int remainingAction(String actorId) {
        PriorityQueue actionQueue = actions.get(actorId);
        if (actionQueue == null) {
            return 0;
        }
        return actionQueue.size();
    }

    @Override
    public void clearAction(String actorId) {
        PriorityQueue actionQueue = actions.remove(actorId);
        if (actionQueue == null) {
            return;
        }
        actionQueue.clear();
    }

}

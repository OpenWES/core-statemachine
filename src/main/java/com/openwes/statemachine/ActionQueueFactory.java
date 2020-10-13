package com.openwes.statemachine;

/**
 *
 * @author xuanloc0511@gmail.com
 * @param <T>
 */
public interface ActionQueueFactory<T extends Action> {

    public void createQueue(String actorId);

    public void enqueueAction(String actorId, T action);

    public T dequeueAction(String actorId);

    public int remainingAction(String actorId);

    public void clearAction(String actorId);
}

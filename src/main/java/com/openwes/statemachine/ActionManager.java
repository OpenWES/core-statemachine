package com.openwes.statemachine;

/**
 *
 * @author xuanloc0511@gmail.com
 */
class ActionManager {

    private final static ActionManager INSTANCE = new ActionManager();

    public final static ActionManager instance() {
        return INSTANCE;
    }

    private ActionQueueFactory actionQueueFactory;

    private ActionManager() {
    }

    void setActionQueueFactory(ActionQueueFactory actionQueueFactory) {
        this.actionQueueFactory = actionQueueFactory;
    }

    ActionQueueFactory getActionQueueFactory() {
        return actionQueueFactory;
    }

    void createQueueIfNotExists(String actorId) {
        actionQueueFactory.createQueue(actorId);
    }

    void enqueueAction(String actorId, Action action) {
        actionQueueFactory.enqueueAction(actorId, action);
    }

    <E extends Action> E dequeueAction(String actorId) {
        return (E) actionQueueFactory.dequeueAction(actorId);
    }

    int remainingAction(String actorId) {
        return actionQueueFactory.remainingAction(actorId);
    }

    void clearAction(String actorId) {
        actionQueueFactory.clearAction(actorId);
    }
}

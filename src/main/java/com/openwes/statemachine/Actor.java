package com.openwes.statemachine;

import com.openwes.core.logging.LogContext;
import java.util.concurrent.atomic.AtomicBoolean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author xuanloc0511@gmail.com
 * @since Sep 1, 2020
 * @version 1.0.0
 *
 */
public class Actor {

    private final static Logger LOGGER = LoggerFactory.getLogger(Actor.class);
    private final AtomicBoolean inProcess = new AtomicBoolean(false);
    private final Object mutex = new Object();
    private final ActorProps props = new ActorProps();
    private final String id;
    private String actorType;
    private String currentState;
    private TransitionLookup lookup;

    public Actor(String id, String currentState) {
        this.id = id;
        this.currentState = currentState;
    }

    public void onStateChange(String from, String to) {
        /**
         * Do nothing by default
         */
    }

    public boolean isCached() {
        return true;
    }

    void setActorType(String actorType) {
        this.actorType = actorType;
    }

    public final ActorProps getProps() {
        return props;
    }

    TransitionLookup getLookup() {
        return lookup;
    }

    Actor setLookup(TransitionLookup lookup) {
        this.lookup = lookup;
        return this;
    }

    final <T extends Actor> T setCurrentState(String currentState) {
        String old = this.currentState;
        this.currentState = currentState;
        onStateChange(old, currentState);
        return (T) this;
    }

    public final String getId() {
        return id;
    }

    public final String getCurrentState() {
        return currentState;
    }

    void createQueueIfNotExists() {
        ActionManager.instance()
                .createQueueIfNotExists(id);
    }

    void enqueueAction(Action action) {
        ActionManager.instance()
                .enqueueAction(id, action);
        nextAction();
    }

    void nextAction() {
        try {
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
                LogContext.set(LogContext.TXID, action.getTxId());
                Transition transition = lookup.lookup(currentState, action.getName());
                if (transition == null) {
                    LOGGER.error("Invalid action {}. Actor {}:{} is in state {}",
                            action.getName(), actorType, id, currentState);
                    if (action.getEndHandler() != null) {
                        action.getEndHandler()
                                .onFailure(id, props, new Failure()
                                        .setCode(Failure.INVALID_ACTION)
                                        .setReason("invalidate action"));
                    }
                    inProcess.set(false);
                    nextAction();
                    return;
                }
                inProcess.set(true);
                Command cmd = new Command()
                        .setActionId(action.getId())
                        .setTxId(action.getTxId())
                        .setActorType(actorType)
                        .setActorId(getId())
                        .setProps(props)
                        .setData(action.getData())
                        .setProcessor(transition.getProcessor())
                        .setEndHandler(action.getEndHandler())
                        .setWatcher(new CommandWatcher() {
                            @Override
                            public void onComplete() {
                                LOGGER.info("Actor {}:{} change state from {} to {}",
                                        actorType, id, currentState, transition.getTo());
                                setCurrentState(transition.getTo());
                                if (transition.isDestroyOnComplete()) {
                                    StateFlowManager.workflow(actorType)
                                            .destroyActor(id);
                                    return;
                                }
                                inProcess.set(false);
                                nextAction();
                            }

                            @Override
                            public void onFail() {
                                LOGGER.info("Process action {} fail. Actor {}:{} keep state {}",
                                        action.getName(), actorType, id, currentState);
                                inProcess.set(false);
                                nextAction();
                            }

                            @Override
                            public void onError(Throwable t) {
                                LOGGER.info("Process action {} get exception. Actor {}:{} keep state {}",
                                        action.getName(), actorType, id, currentState, t);
                                inProcess.set(false);
                                nextAction();
                            }
                        });
                StateFlowManager.instance()
                        .getExecutor()
                        .submit(cmd);
            }
        } catch (InterruptedException e) {
            LOGGER.error("actor {}:{} handle next action get exception ", actorType, id, e);
        }
    }

    Action dequeueAction() {
        return ActionManager.instance()
                .dequeueAction(id);
    }

    void clearAction() {
        ActionManager.instance()
                .clearAction(id);
    }

    public final int remainingAction() {
        return ActionManager.instance()
                .remainingAction(id);
    }
}

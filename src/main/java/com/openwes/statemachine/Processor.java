package com.openwes.statemachine;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author xuanloc0511@gmail.com
 * @param <T>
 * @since Sep 1, 2020
 * @version 1.0.0
 *
 */
public abstract class Processor<T extends Object> {

    private final static Logger LOGGER = LoggerFactory.getLogger(Processor.class);
    private long actionId;
    private String actorType;
    private String actorId;
    private Failure failure;

    public final void setFailure(Failure failure) {
        this.failure = failure;
    }

    public final Failure getFailure() {
        return failure;
    }

    void setActionId(long actionId) {
        this.actionId = actionId;
    }

    void setActorId(String actorId) {
        this.actorId = actorId;
    }

    void setActorType(String actorType) {
        this.actorType = actorType;
    }

    public final String getActorId() {
        return actorId;
    }

    public final String getActorType() {
        return actorType;
    }

    protected final void trigger(String action, Object data) {
        LOGGER.info("Actor {} trigger action {} with id {}", actorId, action, actionId);
        StateFlowManager.workflow(actorType)
                .execute(new Action(actorId, action, data)
                        .setId(actionId));
    }

    public abstract boolean onProcess(ActorProps props, T data);
}

package com.openwes.statemachine;

/**
 *
 * @author xuanloc0511@gmail.com
 * @param <T>
 */
public interface ActionEndHandler<T extends Object> {

    /**
     *
     * @param actorId
     * @param props
     * @param input
     */
    public void onCompleted(String actorId, ActorProps props, T input);

    /**
     *
     * @param actorId
     * @param props
     * @param input
     */
    public void onFailure(String actorId, ActorProps props, Failure input);

    /**
     *
     * @param t
     */
    public void onError(Throwable t);
}

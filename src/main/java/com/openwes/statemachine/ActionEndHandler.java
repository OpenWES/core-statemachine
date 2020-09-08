package com.openwes.statemachine;

/**
 *
 * @author xuanloc0511@gmail.com
 * @param <T>
 */
public interface ActionEndHandler<T extends Object> {

    public void onCompleted(String actorId, ActorProps props, T input);

    public void onFailure(String actorId, ActorProps props, T input);

    public void onError(Throwable t);
}

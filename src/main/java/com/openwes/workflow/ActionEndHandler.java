package com.openwes.workflow;

/**
 *
 * @author xuanloc0511@gmail.com
 * @param <T>
 */
public interface ActionEndHandler<T extends Object> {

    public void onCompleted(String actorId, ActorProps props, T input);
}

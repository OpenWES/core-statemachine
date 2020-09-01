package com.openwes.workflow;

/**
 *
 * @author xuanloc0511@gmail.com
 * @param <T>
 * @since Sep 1, 2020
 * @version 1.0.0
 *
 */
public interface Processor<T extends Object> {

    public void onProcess(String actorId, ActorProps props, T data);
}

package com.openwes.statemachine;

/**
 *
 * @author xuanloc0511@gmail.com
 * @param <I>
 * @since Sep 1, 2020
 * @version 1.0.0
 *
 */
public interface ActorLoader<I extends Action> {

    public Actor load(I action);
}

package com.openwes.workflow;

/**
 *
 * @author xuanloc0511@gmail.com
 * @param <I>
 * @param <O>
 * @since Sep 1, 2020
 * @version 1.0.0
 *
 */
public interface ActorLoader<I extends Action, O extends Actor> {

    public O load(I action);
}

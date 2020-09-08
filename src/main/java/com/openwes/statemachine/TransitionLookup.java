package com.openwes.statemachine;

/**
 *
 * @author xuanloc0511@gmail.com
 */
public interface TransitionLookup {

    public Transition lookup(String state, String actionName);

}

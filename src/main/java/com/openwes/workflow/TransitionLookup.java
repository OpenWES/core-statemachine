package com.openwes.workflow;

/**
 *
 * @author xuanloc0511@gmail.com
 */
public interface TransitionLookup {

    public Transition lookup(String state, String actionName);

}

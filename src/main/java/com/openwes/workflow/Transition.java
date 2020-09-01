package com.openwes.workflow;

/**
 *
 * @author xuanloc0511@gmail.com
 * @since Sep 1, 2020
 * @version 1.0.0
 *
 */
public class Transition {
    
    public final static Transition create(){
        return new Transition();
    }
    
    private Transition(){
    }

    private String from = null;
    private String to = null;
    private String action = null;
    private String processor = null;

    public String getFrom() {
        return from;
    }

    public Transition setFrom(String from) {
        this.from = from;
        return this;
    }

    public String getTo() {
        return to;
    }

    public Transition setTo(String to) {
        this.to = to;
        return this;
    }

    public String getAction() {
        return action;
    }

    public Transition setAction(String action) {
        this.action = action;
        return this;
    }

    public String getProcessor() {
        return processor;
    }

    public Transition setProcessor(String processor) {
        this.processor = processor;
        return this;
    }

}

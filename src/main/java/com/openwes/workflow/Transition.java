package com.openwes.workflow;

/**
 *
 * @author xuanloc0511@gmail.com
 * @since Sep 1, 2020
 * @version 1.0.0
 *
 */
public class Transition {

    public final static Transition from(String from) {
        return new Transition()
                .setFrom(from)
                .setFromAny(false);
    }

    public final static Transition fromAny() {
        return new Transition()
                .setFrom(null)
                .setFromAny(true);
    }

    private Transition() {
    }

    private boolean fromAny = false;
    private String from = null;
    private String to = null;
    private String action = null;
    private String processor = null;

    public boolean isFromAny() {
        return fromAny;
    }

    Transition setFromAny(boolean fromAny) {
        this.fromAny = fromAny;
        return this;
    }

    public String getFrom() {
        return from;
    }

    Transition setFrom(String from) {
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

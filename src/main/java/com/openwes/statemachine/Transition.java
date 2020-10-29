package com.openwes.statemachine;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 *
 * @author xuanloc0511@gmail.com
 * @since Sep 1, 2020
 * @version 1.0.0
 *
 */
public class Transition {

    public final static String FROM_SEPARATOR = "|";

    public final static String PROFILE_DEFAULT = "default";

    public final static Transition createFrom(Transition t) {
        return new Transition()
                .setProfile(t.getProfile())
                .setFrom(t.getFrom())
                .setTo(t.getTo())
                .setProcessor(t.getProcessor())
                .setAction(t.getAction());
    }

    public final static Transition from(String... from) {
        return new Transition()
                .setFrom(StringUtils.join(from, FROM_SEPARATOR))
                .setFromAny(false);
    }

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

    private String profile = PROFILE_DEFAULT;
    private boolean fromAny = false;
    private String from = null;
    private String to = null;
    private String action = null;
    private Class<? extends Processor> processor = null;
    private boolean destroyOnComplete = false;

    public Transition setDestroyOnComplete(boolean destroyOnComplete) {
        this.destroyOnComplete = destroyOnComplete;
        return this;
    }

    public boolean isDestroyOnComplete() {
        return destroyOnComplete;
    }

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

    public Transition setProfile(String profile) {
        this.profile = profile;
        return this;
    }

    public String getProfile() {
        return profile;
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

    public Class<? extends Processor> getProcessor() {
        return processor;
    }

    public Transition setProcessor(Class<? extends Processor> processor) {
        this.processor = processor;
        return this;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}

package com.openwes.workflow;

import com.openwes.core.utils.ClockService;

/**
 *
 * @author xuanloc0511@gmail.com
 */
class Command {

    private final long started = ClockService.nowNS();
    private long actionId;
    private String actorType;
    private String processor;
    private Object data;
    private String actorId;
    private ActorProps props;
    private CommandWatcher watcher;

    public Command setActionId(long actionId) {
        this.actionId = actionId;
        return this;
    }

    public long getActionId() {
        return actionId;
    }

    public ActorProps getProps() {
        return props;
    }

    public Command setProps(ActorProps props) {
        this.props = props;
        return this;
    }

    public String getProcessor() {
        return processor;
    }

    public Command setProcessor(String processor) {
        this.processor = processor;
        return this;
    }

    public Object getData() {
        return data;
    }

    public Command setData(Object data) {
        this.data = data;
        return this;
    }

    public String getActorId() {
        return actorId;
    }

    public Command setActorId(String actorId) {
        this.actorId = actorId;
        return this;
    }

    public Command setActorType(String actorType) {
        this.actorType = actorType;
        return this;
    }

    public String getActorType() {
        return actorType;
    }

    public Command setWatcher(CommandWatcher watcher) {
        this.watcher = watcher;
        return this;
    }

    public long getStarted() {
        return started;
    }

    void complete() {
        watcher.onComplete();
    }

    void fail() {
        watcher.onFail();
    }

    void error(Throwable t) {
        fail();
        watcher.onError(t);
    }

}

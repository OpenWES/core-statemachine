package com.openwes.workflow;

/**
 *
 * @author xuanloc0511@gmail.com
 */
public class Command {

    private String processor;
    private Object data;
    private String actorId;
    private ActorProps props;

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

    void complete() {

    }

    void error(Throwable t) {

    }
}

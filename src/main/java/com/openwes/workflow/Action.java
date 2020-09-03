package com.openwes.workflow;

import com.openwes.core.utils.UniqId;

/**
 *
 * @author xuanloc0511@gmail.com
 * @since Sep 1, 2020
 * @version 1.0.0
 *
 */
public class Action {

    private long id = UniqId.snowflakeId();
    private final String actorId;
    private final String name;
    private final Object data;
    private ActionEndHandler handler;

    public Action(String actorId, String name) {
        this(actorId, name, null);
    }

    public Action(String actorId, String name, Object data) {
        this.actorId = actorId;
        this.name = name;
        this.data = data;
    }

    long getId() {
        return id;
    }

    Action setId(long id) {
        this.id = id;
        return this;
    }

    public Action setHandler(ActionEndHandler handler) {
        this.handler = handler;
        return this;
    }

    public ActionEndHandler getHandler() {
        return handler;
    }

    public String getActorId() {
        return actorId;
    }

    public String getName() {
        return name;
    }

    public Object getData() {
        return data;
    }

}

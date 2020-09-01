package com.openwes.workflow;

import com.openwes.workflow.utils.UniqId;

/**
 *
 * @author xuanloc0511@gmail.com
 * @param <T>
 * @since Sep 1, 2020
 * @version 1.0.0
 *
 */
public class Action<T extends Object> {

    private long id = UniqId.snowflakeId();
    private final String actorId;
    private final String name;
    private final T data;

    public Action(String actorId, String event) {
        this(actorId, event, null);
    }

    public Action(String actorId, String name, T data) {
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

    public String getActorId() {
        return actorId;
    }

    public String getName() {
        return name;
    }

    public T getData() {
        return data;
    }

}

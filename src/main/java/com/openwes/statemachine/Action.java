package com.openwes.statemachine;

import com.google.gson.annotations.Expose;
import com.openwes.core.utils.ClockService;
import com.openwes.core.utils.UniqId;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 *
 * @author xuanloc0511@gmail.com
 * @since Sep 1, 2020
 * @version 1.0.0
 *
 */
public class Action {

    //final properties
    private final long created = ClockService.nowNS();
    private final String actorId;
    private final String name;
    private final Object data;

    //normal properties
    private long id = UniqId.snowflakeId();
    private String txId = UniqId.uniqId16Bytes();
    private ActionEndHandler endHandler;
    private String profile = Transition.PROFILE_DEFAULT;

    public Action(String actorId, String name) {
        this(actorId, name, null);
    }

    public Action(String actorId, String name, Object data) {
        this.actorId = actorId;
        this.name = name;
        this.data = data;
    }

    public Action setProfile(String profile) {
        this.profile = profile;
        return this;
    }

    public String getProfile() {
        return profile;
    }

    public long getCreated() {
        return created;
    }

    public String getTxId() {
        return txId;
    }

    public Action setTxId(String txId) {
        this.txId = txId;
        return this;
    }

    public long getId() {
        return id;
    }

    public Action setId(long id) {
        this.id = id;
        return this;
    }

    public ActionEndHandler getEndHandler() {
        return endHandler;
    }

    public Action setEndHandler(ActionEndHandler endHandler) {
        this.endHandler = endHandler;
        return this;
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

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }

}

package com.openwes.statemachine;

/**
 *
 * @author xuanloc0511@gmail.com
 */
public class Failure {

    public final static int ACTOR_NOT_FOUND = 1,
            INVALID_ACTION = 2;

    private int code;
    private String reason;

    public Failure setCode(int code) {
        this.code = code;
        return this;
    }

    public Failure setReason(String reason) {
        this.reason = reason;
        return this;
    }

    public int getCode() {
        return code;
    }

    public String getReason() {
        return reason;
    }

}

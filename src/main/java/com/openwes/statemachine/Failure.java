package com.openwes.statemachine;

/**
 *
 * @author xuanloc0511@gmail.com
 */
public class Failure {

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

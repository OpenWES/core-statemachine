package com.openwes.workflow;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author xuanloc0511@gmail.com
 */
public class ActorProps {

    private final Map<String, Object> props = new HashMap<>();

    public ActorProps addProp(String key, Object value) {
        props.put(key, value);
        return this;
    }

    public ActorProps addProps(Map<String, Object> props) {
        this.props.putAll(props);
        return this;
    }

    public Object get(String key) {
        return props.get(key);
    }

    public int getInt(String key) {
        return (int) props.get(key);
    }

    public short getShort(String key) {
        return (short) props.get(key);
    }

    public long getLong(String key) {
        return (long) props.get(key);
    }

    public float getFloat(String key) {
        return (float) props.get(key);
    }

    public double getDouble(String key) {
        return (double) props.get(key);
    }

    public boolean getBoolean(String key) {
        return (boolean) props.get(key);
    }
}

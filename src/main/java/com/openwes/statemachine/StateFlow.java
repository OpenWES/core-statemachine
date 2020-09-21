package com.openwes.statemachine;

import com.openwes.core.utils.ClassLoadException;
import com.openwes.core.utils.ClassUtils;
import com.openwes.core.utils.Validate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author xuanloc0511@gmail.com
 * @since Sep 1, 2020
 * @version 1.0.0
 *
 */
public class StateFlow {

    public final static StateFlow create(String actorType) {
        return new StateFlow(actorType);
    }

    private final Map<String, Actor> actors = new HashMap<>();
    private final Map<String, Transition> transitions = new HashMap<>();
    private final TransitionLookup lookup = (String state, String actionName) -> {
        String key = new StringBuilder()
                .append("one://")
                .append(state)
                .append("/")
                .append(actionName)
                .toString();
        Transition t = transitions.get(key);
        if (t == null) {
            key = new StringBuilder()
                    .append("any://")
                    .append(actionName)
                    .toString();
            t = transitions.get(key);
        }
        return t;
    };
    private final String type;
    private String actorLoader;

    private StateFlow(String type) {
        if (Validate.isEmpty(type)) {
            throw new RuntimeException("");
        }
        this.type = type;
    }

    public final String getType() {
        return type;
    }

    public String getActorLoader() {
        return actorLoader;
    }

    public StateFlow setActorLoader(String actorLoader) {
        this.actorLoader = actorLoader;
        return this;
    }

    private StateFlow _addTransition(Transition transition) {
        String key = new StringBuilder()
                .append(transition.isFromAny() ? "any://" : "one://")
                .append(transition.isFromAny() ? "" : transition.getFrom())
                .append(transition.isFromAny() ? "" : "/")
                .append(transition.getAction())
                .toString();
        Transition t = transitions.get(key);
        if (t != null) {
            return this;
        }
        transitions.put(key, transition);
        return this;
    }

    private final boolean hasEmptyElement(String[] froms) {
        for (String from : froms) {
            if (Validate.isEmpty(StringUtils.trimToEmpty(from))) {
                return true;
            }
        }
        return false;
    }

    public StateFlow addTransition(Transition transition) {
        if (transition.isFromAny()) {
            return _addTransition(transition);
        } else {
            String[] froms = StringUtils.split(transition.getFrom(), Transition.FROM_SEPARATOR);
            if (froms.length == 0 || hasEmptyElement(froms)) {
                return _addTransition(transition);
            }
            for (String from : froms) {
                from = StringUtils.trimToEmpty(from);
                _addTransition(Transition.createFrom(transition).setFrom(from));
            }
            return this;
        }
    }

    public StateFlow removeTransition(String from, String actionName) {
        String key;
        if (Validate.isEmpty(from)) {
            key = new StringBuilder("any://")
                    .append(actionName)
                    .toString();
        } else {
            key = new StringBuilder()
                    .append("one://")
                    .append(from)
                    .append("/")
                    .append(actionName)
                    .toString();
        }
        transitions.remove(key);
        return this;
    }

    public List<Transition> getTransitions() {
        return transitions.values()
                .stream()
                .collect(Collectors.toList());
    }

    public void shutdown() {
        actors.forEach((id, actor) -> {
            actor.clearAction();
        });
        actors.clear();
    }

    public StateFlow execute(Action action) {
        Actor actor = actors.get(action.getActorId());
        if (actor == null) {
            try {
                ActorLoader loader = ClassUtils.object(actorLoader);
                actor = loader.load(action);
            } catch (ClassLoadException ex) {
                throw new RuntimeException("");
            }
            if (actor == null) {
                return this;
            }
            actor.setActorType(type);
            actor.setLookup(lookup);
            actors.put(actor.getId(), actor);
        }
        actor.enqueueAction(action);
        return this;
    }
}

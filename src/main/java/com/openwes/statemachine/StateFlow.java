package com.openwes.statemachine;

import com.openwes.core.IOC;
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
    private final TransitionLookup lookup = (String profile, String state, String actionName) -> {
        String key = new StringBuilder()
                .append("one://")
                .append(profile).append("/")
                .append(state)
                .append("/")
                .append(actionName)
                .toString();
        Transition t = transitions.get(key);
        if (t == null) {
            key = new StringBuilder()
                    .append("any://")
                    .append(profile).append("/")
                    .append(actionName)
                    .toString();
            t = transitions.get(key);
        }
        return t;
    };
    private final String type;
    private Class<? super ActorLoader> actorLoader;

    private StateFlow(String type) {
        if (Validate.isEmpty(type)) {
            throw new RuntimeException("");
        }
        this.type = type;
    }

    public final String getType() {
        return type;
    }

    public Class<? super ActorLoader> getActorLoader() {
        return actorLoader;
    }

    public <T extends ActorLoader> StateFlow setActorLoader(Class<T> actorLoader) {
        this.actorLoader = (Class<? super ActorLoader>) actorLoader;
        return this;
    }

    private StateFlow _addTransition(Transition transition) {
        String key = new StringBuilder()
                .append(transition.isFromAny() ? "any://" : "one://")
                .append(transition.getProfile()).append("/")
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

    private boolean hasEmptyElement(String[] froms) {
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
            if (froms.length == 1 || hasEmptyElement(froms)) {
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
        return removeTransition(Transition.PROFILE_DEFAULT, from, actionName);
    }

    public StateFlow removeTransition(String profile, String from, String actionName) {
        String key;
        if (Validate.isEmpty(from)) {
            key = new StringBuilder("any://")
                    .append(profile).append("/")
                    .append(actionName)
                    .toString();
        } else {
            key = new StringBuilder()
                    .append("one://")
                    .append(profile).append("/")
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

    final void destroyActor(String actorId) {
        Actor actor = actors.remove(actorId);
        if (actor != null) {
            actor.clearAction();
        }
    }

    public final Actor actor(String actorId) {
        return actors.get(actorId);
    }

    public synchronized StateFlow execute(Action action) {
        Actor actor = actors.get(action.getActorId());
        if (actor == null) {
            try {
                ActorLoader loader = (ActorLoader) IOC.init(actorLoader);
                actor = loader.load(action);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
            if (actor == null) {
                if (action.getEndHandler() != null) {
                    action.getEndHandler().onFailure(
                            action.getActorId(),
                            new ActorProps(),
                            new Failure()
                                    .setCode(Failure.ACTOR_NOT_FOUND)
                                    .setReason("actor not found"));
                }
                return this;
            }
            actor.setActorType(type);
            actor.setLookup(lookup);
            if (actor.isCached()) {
                actors.put(actor.getId(), actor);
            }
            actor.createQueueIfNotExists();
        }
        actor.enqueueAction(action);
        return this;
    }
}

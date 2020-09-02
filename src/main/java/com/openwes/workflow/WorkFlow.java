package com.openwes.workflow;

import com.openwes.workflow.utils.ClassLoadException;
import com.openwes.workflow.utils.ClassUtils;
import com.openwes.workflow.utils.Validate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *
 * @author xuanloc0511@gmail.com
 * @since Sep 1, 2020
 * @version 1.0.0
 *
 */
public class WorkFlow {

    public final static WorkFlow create(String actorType) {
        return new WorkFlow(actorType);
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

    private WorkFlow(String type) {
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

    public WorkFlow setActorLoader(String actorLoader) {
        this.actorLoader = actorLoader;
        return this;
    }

    public WorkFlow addTransition(Transition transition) {
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

    public WorkFlow execute(Action action) {
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

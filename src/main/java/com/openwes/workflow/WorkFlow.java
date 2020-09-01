package com.openwes.workflow;

import com.openwes.workflow.utils.ClassLoadException;
import com.openwes.workflow.utils.ClassUtils;
import com.openwes.workflow.utils.Validate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        return this;
    }
    
    public List<Transition> getTransitions(){
        return new ArrayList<>();
    }

    public void shutdown() {
        actors.forEach((id, actor) -> {
            actor.clearAction();
        });
        actors.clear();
    }

    public void execute(Action action) {
        Actor actor = actors.get(action.getActorId());
        if (actor == null) {
            try {
                ActorLoader loader = ClassUtils.object(actorLoader);
                actor = loader.load(action);
            } catch (ClassLoadException ex) {
            }
            if (actor == null) {
                return;
            }
            actors.put(actor.getId(), actor);
        }
        actor.enqueueAction(action);
    }
}

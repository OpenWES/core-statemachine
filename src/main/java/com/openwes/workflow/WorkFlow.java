package com.openwes.workflow;

import com.openwes.workflow.utils.ClassLoadException;
import com.openwes.workflow.utils.ClassUtils;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author xuanloc0511@gmail.com
 * @param <T>
 * @since Sep 1, 2020
 * @version 1.0.0
 *
 */
public class WorkFlow<T extends Actor> {

    public final static <T extends Actor> WorkFlow create(Class<T> clzz) {
        return new WorkFlow(clzz);
    }

    private final Class<T> type;
    private final Map<String, Actor> actors = new HashMap<>();
    private String actorLoader;

    private WorkFlow(Class<T> type) {
        if (type == null) {
            throw new RuntimeException("");
        }
        this.type = type;
    }

    public final String getType() {
        return type.getName();
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

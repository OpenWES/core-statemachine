
import com.openwes.workflow.Action;
import com.openwes.workflow.Actor;
import com.openwes.workflow.ActorLoader;

/**
 *
 * @author xuanloc0511@gmail.com
 */
public class TestActorLoader implements ActorLoader<Action> {

    @Override
    public Actor load(Action action) {
        return new TestActor();
    }

}

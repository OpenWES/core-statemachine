
import com.openwes.workflow.Action;
import com.openwes.workflow.ActorLoader;

/**
 *
 * @author xuanloc0511@gmail.com
 */
public class TestActorLoader implements ActorLoader<Action, TestActor>{

    @Override
    public TestActor load(Action action) {
        return new TestActor();
    }

}

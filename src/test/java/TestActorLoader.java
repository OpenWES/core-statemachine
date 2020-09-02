
import com.openwes.workflow.Action;
import com.openwes.workflow.Actor;
import com.openwes.workflow.ActorLoader;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 * @author xuanloc0511@gmail.com
 */
public class TestActorLoader implements ActorLoader<Action> {

    public final static AtomicInteger counter = new AtomicInteger(1);

    @Override
    public Actor load(Action action) {
        return new TestActor()
                .setId(counter.getAndIncrement() + "")
                .setCurrentState("A");
    }

}

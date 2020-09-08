
import com.openwes.statemachine.Action;
import com.openwes.statemachine.Actor;
import com.openwes.statemachine.ActorLoader;
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

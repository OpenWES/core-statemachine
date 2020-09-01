
import com.openwes.workflow.Action;
import com.openwes.workflow.Transition;
import com.openwes.workflow.WorkFlow;
import com.openwes.workflow.WorkFlowManager;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author xuanloc0511@gmail.com
 */
public class TestWorkFlow {

    @Before
    public void beforeTest() {
        WorkFlowManager.instance()
                .register(WorkFlow.create(TestActor.class)
                        .setActorLoader(TestActorLoader.class.getName())
                        .addTransition(Transition.create()));

    }

    @Test
    public void test() {
        WorkFlowManager.instance()
                .workFlow(TestActor.class)
                .execute(new Action("1", "LOGIN"));
    }

    @After
    public void afterTest() {

    }
}

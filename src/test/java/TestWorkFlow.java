
import com.openwes.workflow.Action;
import com.openwes.workflow.Transition;
import com.openwes.workflow.WorkFlow;
import com.openwes.workflow.WorkFlowManager;
import com.openwes.workflow.utils.UniqId;
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
        UniqId.instance().initSnowFlakeIdGenerator(1);
        WorkFlowManager.instance()
                .register(WorkFlow.create(TestActor.class.getName())
                        .setActorLoader(TestActorLoader.class.getName())
                        .addTransition(Transition.from("A")
                                .setAction("ACTION_1")
                                .setTo("B")
                                .setProcessor(ProcessActionA2B.class.getName()))
                        .addTransition(Transition.from("B")
                                .setAction("ACTION_2")
                                .setTo("C"))
                        .addTransition(Transition.from("C")
                                .setAction("ACTION_3")
                                .setTo("D"))
                        .addTransition(Transition.from("A")
                                .setAction("ACTION_4")
                                .setTo("C"))
                        .addTransition(Transition.fromAny()
                                .setAction("ACTION_5")
                                .setTo("D")));
    }

    @Test
    public void test() {
        WorkFlowManager.instance()
                .workFlow(TestActor.class.getName())
                .execute(new Action("1", "ACTION_2", null));
    }

    @After
    public void afterTest() {
        WorkFlowManager.instance()
                .unregister(TestActor.class.getName())
                .shutdown();
    }
}

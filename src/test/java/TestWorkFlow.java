
import com.openwes.core.utils.UniqId;
import com.openwes.workflow.Action;
import com.openwes.workflow.Transition;
import com.openwes.workflow.WorkFlow;
import com.openwes.workflow.WorkFlowManager;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author xuanloc0511@gmail.com
 */
@RunWith(JUnit4.class)
public class TestWorkFlow {

    private final static Logger LOGGER = LoggerFactory.getLogger(TestWorkFlow.class);

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
                                .setTo("C")
                                .setProcessor(ProcessActionB2C.class.getName()))
                        .addTransition(Transition.from("C")
                                .setAction("ACTION_3")
                                .setTo("D")
                                .setProcessor(ProcessActionC2D.class.getName()))
                        .addTransition(Transition.from("A")
                                .setAction("ACTION_4")
                                .setTo("C")
                                .setProcessor(ProcessActionC2D.class.getName()))
                        .addTransition(Transition.fromAny()
                                .setAction("ACTION_5")
                                .setTo("D")
                                .setProcessor(ProcessActionAny2D.class.getName())))
                .start();
    }

    @Test
    public void test() throws InterruptedException {
        LOGGER.info("Running test...");
        WorkFlowManager.workflow(TestActor.class.getName())
                .execute(new Action("1", "ACTION_2", null))
                .execute(new Action("2", "ACTION_1", null))
                .execute(new Action("2", "ACTION_2", null))
                .execute(new Action("2", "ACTION_3", null))
                .execute(new Action("2", "ACTION_4", null))
                .execute(new Action("2", "ACTION_5", null));
        Thread.sleep(2000);
    }

    @After
    public void afterTest() {
        WorkFlowManager.instance()
                .unregister(TestActor.class.getName())
                .shutdown();
    }
}

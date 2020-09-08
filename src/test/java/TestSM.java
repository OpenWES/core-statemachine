
import com.openwes.test.ProcessActionA2B;
import com.openwes.test.ProcessActionAny2D;
import com.openwes.test.ProcessActionC2D;
import com.openwes.test.ProcessActionB2C;
import com.openwes.core.Application;
import com.openwes.statemachine.Action;
import com.openwes.statemachine.Transition;
import com.openwes.statemachine.StateFlow;
import com.openwes.statemachine.StateFlowManager;
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
public class TestSM {

    private final static Logger LOGGER = LoggerFactory.getLogger(TestSM.class);

    @Before
    public void beforeTest() {
        StateFlowManager.instance()
                .register(StateFlow.create(TestActor.class.getName())
                        .setActorLoader(TestActorLoader.class.getName())
                        .addTransition(Transition.from("A")
                                .setAction("ACTION_1")
                                .setTo("B")
                                .setProcessor(ProcessActionA2B.class))
                        .addTransition(Transition.from("B")
                                .setAction("ACTION_2")
                                .setTo("C")
                                .setProcessor(ProcessActionB2C.class))
                        .addTransition(Transition.from("C")
                                .setAction("ACTION_3")
                                .setTo("D")
                                .setProcessor(ProcessActionC2D.class))
                        .addTransition(Transition.from("A")
                                .setAction("ACTION_4")
                                .setTo("C")
                                .setProcessor(ProcessActionC2D.class))
                        .addTransition(Transition.fromAny()
                                .setAction("ACTION_5")
                                .setTo("D")
                                .setProcessor(ProcessActionAny2D.class)));
        Application.run();
    }

    @Test
    public void test() throws InterruptedException {
        LOGGER.info("Running test...");
        StateFlowManager.workflow(TestActor.class.getName())
                .execute(new Action("1", "ACTION_2", null))
                .execute(new Action("2", "ACTION_1", null))
                .execute(new Action("2", "ACTION_2", null))
                .execute(new Action("2", "ACTION_3", null))
                .execute(new Action("2", "ACTION_4", null))
                .execute(new Action("2", "ACTION_5", null));
        Thread.sleep(1000);
    }

    @After
    public void afterTest() {
    }
}

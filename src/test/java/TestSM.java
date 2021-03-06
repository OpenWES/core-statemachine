
import com.openwes.test.ProcessActionA2B;
import com.openwes.test.ProcessActionAny2D;
import com.openwes.test.ProcessActionC2D;
import com.openwes.test.ProcessActionB2C;
import com.openwes.core.Application;
import com.openwes.statemachine.Action;
import com.openwes.statemachine.Transition;
import com.openwes.statemachine.StateFlow;
import com.openwes.statemachine.StateFlowManager;
import com.openwes.test.ProcessActionAB2D;
import com.openwes.test.ProcessActionB2CCustomized;
import org.junit.After;
import org.junit.Assert;
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
                        .setActorLoader(TestActorLoader.class)
                        .addTransition(Transition.from("A")
                                .setAction("ACTION_1")
                                .setTo("B")
                                .setProcessor(ProcessActionA2B.class))
                        .addTransition(Transition.from("B")
                                .setAction("ACTION_2")
                                .setTo("C")
                                .setProcessor(ProcessActionB2C.class))
                        .addTransition(Transition.from("B")
                                .setAction("ACTION_2")
                                .setProfile("customized")
                                .setTo("D")
                                .setProcessor(ProcessActionB2CCustomized.class))
                        .addTransition(Transition.from("C")
                                .setAction("ACTION_3")
                                .setTo("D")
                                .setProcessor(ProcessActionC2D.class))
                        .addTransition(Transition.from("A")
                                .setAction("ACTION_4")
                                .setTo("C")
                                .setProcessor(ProcessActionC2D.class))
                        .addTransition(Transition.from("A", "B")
                                .setAction("ACTION_6")
                                .setTo("D")
                                .setProcessor(ProcessActionAB2D.class)
                                .setDestroyOnComplete(true))
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
                .execute(new Action("2", "ACTION_5", null))
                .execute(new Action("3", "ACTION_1", null))
                .execute(new Action("3", "ACTION_6", null))
                .execute(new Action("4", "ACTION_1", null))
                .execute(new Action("4", "ACTION_2", null).setProfile("customized"));
        Thread.sleep(2000);

        {
            String currentState = StateFlowManager.workflow(TestActor.class.getName())
                    .actor("1").getCurrentState();
            Assert.assertEquals("Actor 1 is at state A", "A", currentState);
        }

        {
            String currentState = StateFlowManager.workflow(TestActor.class.getName())
                    .actor("2").getCurrentState();
            Assert.assertEquals("Actor 2 is at state D", "D", currentState);
        }

        {
            String currentState = StateFlowManager.workflow(TestActor.class.getName())
                    .actor("3").getCurrentState();
            Assert.assertEquals("Actor 3 is at state D", "D", currentState);
        }

        {
            String currentState = StateFlowManager.workflow(TestActor.class.getName())
                    .actor("4").getCurrentState();
            Assert.assertEquals("Actor 4 is at state D", "D", currentState);
        }
    }

    @After
    public void afterTest() {
    }
}

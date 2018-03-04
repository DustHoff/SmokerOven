package de.onesi.hoffnet.test;

import de.onesi.hoffnet.events.OvenEvent;
import de.onesi.hoffnet.states.OvenState;
import de.onesi.hoffnet.tinkerforge.io.OvenPlug;
import de.onesi.hoffnet.tinkerforge.io.SmokerPlug;
import org.junit.After;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.statemachine.StateMachine;
import org.springframework.test.context.ActiveProfiles;


@SpringBootTest()
@ActiveProfiles("test")
public class BasicTest {
    @Autowired
    protected StateMachine<OvenState, OvenEvent> ovenStateMachine;
    @Autowired
    private OvenPlug ovenPlug;
    @Autowired
    private SmokerPlug smokerPlug;

    @Before
    public void before() throws Exception {
        ovenStateMachine.addStateListener(ovenPlug);
        ovenStateMachine.addStateListener(smokerPlug);
        ovenStateMachine.start();
        sendEvent(OvenEvent.INITIALIZED, OvenState.READY);
    }

    @After
    public void after() throws Exception {
        ovenStateMachine.stop();
        ovenStateMachine.getExtendedState().getVariables().clear();
        Thread.sleep(1000);
    }

    public void sendEvent(OvenEvent event, OvenState expected) throws Exception {
        for (int i = 0; i < 400; i++) {
            if (ovenStateMachine.getState().getId() == expected) break;
            ovenStateMachine.sendEvent(event);
            Thread.sleep(200);
        }
    }
}

package de.onesi.hoffnet.test;

import de.onesi.hoffnet.events.OvenEvent;
import de.onesi.hoffnet.states.OvenState;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class InitializationTest extends TFMock {

    @Test
    public void stateReadyTest() {
        Assert.assertEquals(OvenState.READY, ovenStateMachine.getState().getId());
    }

    @Test
    public void stateBusyTest() throws Exception {
        ovenStateMachine.sendEvent(OvenEvent.CONFIGURED);
        Assert.assertEquals(OvenState.BUSY, ovenStateMachine.getState().getId());
    }
}

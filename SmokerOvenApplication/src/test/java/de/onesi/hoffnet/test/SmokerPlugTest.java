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
public class SmokerPlugTest extends TFMock {

    @Test
    public void startSmoker() {
        Assert.assertEquals(OvenState.READY, ovenStateMachine.getState().getId());
        Assert.assertFalse("SmokerPlug initial turn Off", smokerPlug.getState());
        ovenStateMachine.sendEvent(OvenEvent.CONFIGURED);
        Assert.assertEquals(OvenState.BUSY, ovenStateMachine.getState().getId());
        Assert.assertTrue("SmokerPlug turn On", smokerPlug.getState());
    }

    @Test
    public void stopSmoker() {
        startSmoker();
        ovenStateMachine.sendEvent(OvenEvent.TEMPERATURE_REACHED);
        Assert.assertFalse("SmokerPlug turn Off", smokerPlug.getState());
    }
}

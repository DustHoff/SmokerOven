package de.onesi.hoffnet.test;

import de.onesi.hoffnet.events.OvenEvent;
import de.onesi.hoffnet.states.OvenState;
import de.onesi.hoffnet.tinkerforge.io.SmokerPlug;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class SmokerPlugTest extends BasicTest {

    @Autowired
    private SmokerPlug smokerPlug;

    @Override
    public void before() throws Exception {
        super.before();
        smokerPlug.turnOff();
    }

    @Test
    public void startSmoker() throws Exception {
        sendEvent(OvenEvent.CONFIGURED, OvenState.BUSY);
        Assert.assertTrue("SmokerPlug turn On", smokerPlug.getState());
    }

    @Test
    public void stopSmoker() throws Exception {
        startSmoker();
        ovenStateMachine.sendEvent(OvenEvent.TEMPERATURE_REACHED);
        Assert.assertFalse("SmokerPlug turn Off", smokerPlug.getState());
    }
}

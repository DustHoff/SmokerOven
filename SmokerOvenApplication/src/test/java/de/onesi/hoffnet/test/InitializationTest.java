package de.onesi.hoffnet.test;

import de.onesi.hoffnet.events.OvenEvent;
import de.onesi.hoffnet.states.OvenState;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Calendar;
import java.util.GregorianCalendar;

@RunWith(SpringRunner.class)
@SpringBootTest
public class InitializationTest extends TFMock {

    private Calendar calendar;

    @Test
    public void stateReadyTest() {
        Assert.assertEquals(OvenState.READY, ovenStateMachine.getState().getId());
    }

    @Test
    public void statePrepaireWaitTest() throws Exception {
        calendar = GregorianCalendar.getInstance();
        calendar.add(Calendar.MINUTE, 1);
        ovenStateMachine.getExtendedState().getVariables().put("start", calendar.getTime());
        ovenStateMachine.sendEvent(OvenEvent.CONFIGURED);
        Assert.assertEquals(OvenState.PREPAIRE_WAIT, ovenStateMachine.getState().getId());
        Thread.sleep(61000);
        Assert.assertEquals(OvenState.BUSY, ovenStateMachine.getState().getId());
    }

    @Test
    public void stateBusyTest() throws Exception {
        ovenStateMachine.sendEvent(OvenEvent.CONFIGURED);
        Assert.assertEquals(OvenState.BUSY, ovenStateMachine.getState().getId());
    }

    @Test
    public void stateFinishedReady() {
        ovenStateMachine.sendEvent(OvenEvent.CONFIGURED);
        Assert.assertEquals(OvenState.BUSY, ovenStateMachine.getState().getId());
        ovenStateMachine.sendEvent(OvenEvent.TEMPERATURE_REACHED);
        Assert.assertEquals(OvenState.READY, ovenStateMachine.getState().getId());
    }
}

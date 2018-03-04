package de.onesi.hoffnet.test;

import de.onesi.hoffnet.events.OvenEvent;
import de.onesi.hoffnet.states.OvenState;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Calendar;
import java.util.GregorianCalendar;

@RunWith(SpringRunner.class)
public class InitializationTest extends BasicTest {

    private Calendar calendar;

    @Test
    public void stateReadyTest() {
        Assert.assertEquals(OvenState.READY, ovenStateMachine.getState().getId());
    }

    @Test
    public void statePrepaireWaitTest() throws Exception {
        Assert.assertEquals(OvenState.READY, ovenStateMachine.getState().getId());
        calendar = GregorianCalendar.getInstance();
        calendar.add(Calendar.SECOND, 50);
        ovenStateMachine.getExtendedState().getVariables().put("start", calendar.getTime());
        Thread.sleep(1000);
        ovenStateMachine.sendEvent(OvenEvent.CONFIGURED);
        Assert.assertEquals(OvenState.PREPAIRE_WAIT, ovenStateMachine.getState().getId());
        Thread.sleep(60000);
        Assert.assertEquals(OvenState.BUSY, ovenStateMachine.getState().getId());
    }

    @Test
    public void stateBusyTest() throws Exception {
        Assert.assertEquals(OvenState.READY, ovenStateMachine.getState().getId());
        ovenStateMachine.sendEvent(OvenEvent.CONFIGURED);
        Thread.sleep(500);
        Assert.assertEquals(OvenState.BUSY, ovenStateMachine.getState().getId());
    }

    @Test
    public void stateFinishedReady() throws Exception {
        Assert.assertEquals(OvenState.READY, ovenStateMachine.getState().getId());
        ovenStateMachine.sendEvent(OvenEvent.CONFIGURED);
        Thread.sleep(500);
        Assert.assertEquals(OvenState.BUSY, ovenStateMachine.getState().getId());
        Thread.sleep(500);
        ovenStateMachine.sendEvent(OvenEvent.TEMPERATURE_REACHED);
        Assert.assertEquals(OvenState.READY, ovenStateMachine.getState().getId());
    }
}

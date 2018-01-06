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
public class ObjectTemperatureTest extends TFMock {

    @Override
    public void before() throws Exception {
        super.before();
        ovenStateMachine.sendEvent(OvenEvent.CONFIGURED);
        objectTemperatureSensor.setTolerance(2d);
        objectTemperatureSensor.setTargetTemperature(92d);
        Assert.assertEquals(OvenState.BUSY, ovenStateMachine.getState().getId());
    }

    @Test
    public void keepHeating() {
        objectTemperatureSensor.temperature(6000);
        Assert.assertEquals(60d, objectTemperatureSensor.getTemperature(), 0);
        Assert.assertEquals(OvenState.BUSY, ovenStateMachine.getState().getId());
        objectTemperatureSensor.temperature(8535);
        Assert.assertEquals(85.35d, objectTemperatureSensor.getTemperature(), 0);
        Assert.assertEquals(OvenState.BUSY, ovenStateMachine.getState().getId());
    }

    @Test
    public void temperatureReached() {
        objectTemperatureSensor.temperature(9200);
        Assert.assertEquals(92d, objectTemperatureSensor.getTemperature(), 0);
        Assert.assertEquals(OvenState.READY, ovenStateMachine.getState().getId());

    }
}

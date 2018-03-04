package de.onesi.hoffnet.test;

import de.onesi.hoffnet.events.OvenEvent;
import de.onesi.hoffnet.states.OvenState;
import de.onesi.hoffnet.tinkerforge.sensor.ObjectTemperatureSensor;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class ObjectTemperatureTest extends BasicTest {

    @Autowired
    private ObjectTemperatureSensor objectTemperatureSensor;

    @Override
    public void before() throws Exception {
        super.before();
        ovenStateMachine.sendEvent(OvenEvent.CONFIGURED);
        objectTemperatureSensor.setTolerance(2d);
        objectTemperatureSensor.setTargetTemperature(92d);
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

    @Test
    public void setMaxTemperature() {
        objectTemperatureSensor.setTargetTemperature(300d);
        Assert.assertEquals(150d, objectTemperatureSensor.getTargetTemperature(), 0d);
    }
}

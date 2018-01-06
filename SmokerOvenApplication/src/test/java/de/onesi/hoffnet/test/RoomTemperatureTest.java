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
public class RoomTemperatureTest extends TFMock {

    @Override
    public void before() throws Exception {
        super.before();
        ovenStateMachine.sendEvent(OvenEvent.CONFIGURED);
        Assert.assertEquals(OvenState.BUSY, ovenStateMachine.getState().getId());
        roomTemperatureSensor.setTargetTemperature(90);
    }

    @Test
    public void temperatureChanged() {
        roomTemperatureSensor.temperature(6553);
        Assert.assertEquals(65.53d,roomTemperatureSensor.getTemperature(),0d);
    }

    @Test
    public void ovenKeepHeating() {
        roomTemperatureSensor.temperature(8500);
        Assert.assertEquals(85d,roomTemperatureSensor.getTemperature(),0d);
        Assert.assertTrue("Plug turned on", ovenPlug.getState());
    }

    @Test
    public void ovenRoomTemperatureReached() {
        roomTemperatureSensor.temperature(9000);
        Assert.assertEquals(90d,roomTemperatureSensor.getTemperature(),0d);
        Assert.assertTrue("Plug turned on", ovenPlug.getState());
        roomTemperatureSensor.temperature(9350);
        Assert.assertEquals(93.50d,roomTemperatureSensor.getTemperature(),0d);
        Assert.assertFalse("Plug turned off", ovenPlug.getState());
    }

    @Test
    public void ovenRoomTemperatureCooled(){
        roomTemperatureSensor.temperature(9200);
        Assert.assertEquals(92d,roomTemperatureSensor.getTemperature(),0d);
        Assert.assertFalse("Plug turned off", ovenPlug.getState());
        roomTemperatureSensor.temperature(7700);
        Assert.assertEquals(77d,roomTemperatureSensor.getTemperature(),0d);
        Assert.assertTrue("Plug turned on", ovenPlug.getState());
    }
}

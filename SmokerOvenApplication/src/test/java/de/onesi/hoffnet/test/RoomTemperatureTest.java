package de.onesi.hoffnet.test;

import de.onesi.hoffnet.events.OvenEvent;
import de.onesi.hoffnet.states.OvenState;
import de.onesi.hoffnet.tinkerforge.io.OvenPlug;
import de.onesi.hoffnet.tinkerforge.sensor.RoomTemperatureSensor;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class RoomTemperatureTest extends BasicTest {

    @Autowired
    private RoomTemperatureSensor roomTemperatureSensor;
    @Autowired
    private OvenPlug ovenPlug;

    @Override
    public void before() throws Exception {
        super.before();
        roomTemperatureSensor.setTargetTemperature(90);
        //ovenPlug.turnOff();
        sendEvent(OvenEvent.CONFIGURED, OvenState.BUSY);
    }

    @Test
    public void temperatureChanged() {
        roomTemperatureSensor.temperature(6553);
        Assert.assertEquals(65.53d, roomTemperatureSensor.getTemperature(), 0d);
    }

    @Test
    public void ovenKeepHeating() throws InterruptedException {
        roomTemperatureSensor.temperature(8500);
        Thread.sleep(500);
        Assert.assertEquals(85d, roomTemperatureSensor.getTemperature(), 0d);
        Assert.assertTrue("Plug turned on", ovenPlug.getState());
    }

    @Test
    public void ovenRoomTemperatureReached() throws InterruptedException {
        Assert.assertTrue("Plug turned on", ovenPlug.getState());
        roomTemperatureSensor.temperature(9350);
        Thread.sleep(500);
        Assert.assertEquals(93.50d, roomTemperatureSensor.getTemperature(), 0d);
        Assert.assertFalse("Plug turned off", ovenPlug.getState());
    }

    @Test
    public void ovenRoomTemperatureCooled() throws InterruptedException {
        roomTemperatureSensor.temperature(9000);
        Thread.sleep(500);
        Assert.assertEquals(90d, roomTemperatureSensor.getTemperature(), 0d);
        Assert.assertFalse("Plug turned off", ovenPlug.getState());
        roomTemperatureSensor.temperature(7700);
        Thread.sleep(500);
        Assert.assertEquals(77d, roomTemperatureSensor.getTemperature(), 0d);
        Assert.assertTrue("Plug turned on", ovenPlug.getState());
    }

    @Test
    public void setMaxTemperature() {
        roomTemperatureSensor.setTargetTemperature(300d);
        Assert.assertEquals(150d, roomTemperatureSensor.getTargetTemperature(), 0d);
    }
}

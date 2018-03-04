package de.onesi.hoffnet.test;

import de.onesi.hoffnet.events.OvenEvent;
import de.onesi.hoffnet.states.OvenState;
import de.onesi.hoffnet.tinkerforge.TFConnection;
import de.onesi.hoffnet.tinkerforge.io.OvenPlug;
import de.onesi.hoffnet.tinkerforge.io.SmokerPlug;
import de.onesi.hoffnet.tinkerforge.sensor.ObjectTemperatureSensor;
import de.onesi.hoffnet.tinkerforge.sensor.RoomTemperatureSensor;
import org.mockito.Matchers;
import org.mockito.Mockito;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.state.State;

import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.doNothing;

@Configuration
@Profile("test")
public class TestConfiguration {
    @Autowired
    protected ApplicationContext context;

    @Bean(name = "OvenPlug")
    @Primary
    public OvenPlug getOvenPlug() throws Exception {
        OvenPlug plug = Mockito.mock(OvenPlug.class);
        doNothing().when(plug).initialize();
        doNothing().when(plug).sendState();
        doCallRealMethod().when(plug).turnOn();
        doCallRealMethod().when(plug).turnOff();
        doCallRealMethod().when(plug).getState();
        doCallRealMethod().when(plug).stateEntered(Matchers.any(State.class));
        doCallRealMethod().when(plug).setLog(Matchers.any());
        plug.setLog(LoggerFactory.getLogger(OvenPlug.class));
        plug.turnOff();
        return plug;
    }

    @Bean(name = "SmokerPlug")
    @Primary
    public SmokerPlug getSmokerPlug() throws Exception {
        SmokerPlug plug = Mockito.mock(SmokerPlug.class);
        doNothing().when(plug).initialize();
        doNothing().when(plug).sendState();
        doCallRealMethod().when(plug).turnOn();
        doCallRealMethod().when(plug).turnOff();
        doCallRealMethod().when(plug).getState();
        doCallRealMethod().when(plug).stateEntered(Matchers.any(State.class));
        doCallRealMethod().when(plug).setLog(Matchers.any());
        plug.setLog(LoggerFactory.getLogger(SmokerPlug.class));
        plug.turnOff();
        return plug;
    }

    @Bean(name = "ObjectTemperatureSensor")
    @Primary
    public ObjectTemperatureSensor getObjectTemperatureSensor() throws Exception {
        ObjectTemperatureSensor sensor = Mockito.mock(ObjectTemperatureSensor.class);
        doNothing().when(sensor).initialize();
        doCallRealMethod().when(sensor).getTemperature();
        doCallRealMethod().when(sensor).temperature(Matchers.anyInt());
        doCallRealMethod().when(sensor).getTargetTemperature();
        doCallRealMethod().when(sensor).setTargetTemperature(Matchers.anyDouble());
        doCallRealMethod().when(sensor).setMaxTemperature(Matchers.anyDouble());
        doCallRealMethod().when(sensor).setLog(Matchers.any());
        sensor.setLog(LoggerFactory.getLogger(RoomTemperatureSensor.class));
        sensor.setMaxTemperature(150d);
        return sensor;
    }

    @Bean(name = "RoomTemperatureSensor")
    @Primary
    public RoomTemperatureSensor getRoomTemperatureSensor() throws Exception {
        RoomTemperatureSensor sensor = Mockito.mock(RoomTemperatureSensor.class);
        doNothing().when(sensor).initialize();
        doCallRealMethod().when(sensor).getTemperature();
        doCallRealMethod().when(sensor).temperature(Matchers.anyInt());
        doCallRealMethod().when(sensor).getTargetTemperature();
        doCallRealMethod().when(sensor).setTargetTemperature(Matchers.anyDouble());
        doCallRealMethod().when(sensor).evaluate((StateContext<OvenState, OvenEvent>) Matchers.any());
        doCallRealMethod().when(sensor).setMaxTemperature(Matchers.anyDouble());
        doCallRealMethod().when(sensor).setLog(Matchers.any());
        sensor.setLog(LoggerFactory.getLogger(RoomTemperatureSensor.class));
        sensor.setMaxTemperature(150d);
        return sensor;
    }

    @Bean(name = "TFConnection")
    @Primary
    public TFConnection getConnection() throws Exception {
        TFConnection connection = Mockito.mock(TFConnection.class);
        doNothing().when(connection).execute(Matchers.any());
        doNothing().when(connection).connect();
        doCallRealMethod().when(connection).setApplicationContext(Matchers.any());
        doCallRealMethod().when(connection).getOvenStateMachine();
        connection.setApplicationContext(context);
        return connection;
    }
}

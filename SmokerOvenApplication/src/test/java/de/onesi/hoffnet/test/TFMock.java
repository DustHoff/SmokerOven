package de.onesi.hoffnet.test;

import de.onesi.hoffnet.SmokerOven;
import de.onesi.hoffnet.events.OvenEvent;
import de.onesi.hoffnet.states.OvenState;
import de.onesi.hoffnet.tinkerforge.TFConnection;
import de.onesi.hoffnet.tinkerforge.io.OvenPlug;
import de.onesi.hoffnet.tinkerforge.io.SmokerPlug;
import de.onesi.hoffnet.tinkerforge.sensor.ObjectTemperatureSensor;
import de.onesi.hoffnet.tinkerforge.sensor.RoomTemperatureSensor;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.state.State;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.doNothing;

public class TFMock {

    @Autowired
    protected ApplicationContext context;
    @MockBean
    protected TFConnection connection;
    @MockBean
    protected OvenPlug ovenPlug;
    @MockBean
    protected SmokerPlug smokerPlug;
    @MockBean
    protected RoomTemperatureSensor roomTemperatureSensor;
    @MockBean
    protected ObjectTemperatureSensor objectTemperatureSensor;
    @InjectMocks
    @Autowired
    protected SmokerOven smokerOven;
    @InjectMocks
    @Autowired
    protected StateMachine<OvenState, OvenEvent> ovenStateMachine;

    @Before
    public void before() throws Exception {
        mockConnection();
        mockPlugs();
        mockTemperatureSensor();
        MockitoAnnotations.initMocks(this);
        ovenStateMachine.getExtendedState().getVariables().clear();
        ovenStateMachine.start();
        connection.connected((short) 1);
    }

    @After
    public void after() {
        ovenStateMachine.stop();
    }

    private void mockConnection() throws Exception {
        //given(connection.getConnectionState()).willReturn(ConnectionState.CONNECTED.getId());
        doNothing().when(connection).connect();
        doCallRealMethod().when(connection).getState();
        doCallRealMethod().when(connection).connected(Matchers.anyShort());
        doCallRealMethod().when(connection).execute(Matchers.any(StateContext.class));
        doCallRealMethod().when(connection).setApplicationContext(Matchers.any(ApplicationContext.class));
        doCallRealMethod().when(connection).setLog(Matchers.any(Logger.class));
        given(connection.getOvenStateMachine()).willReturn((StateMachine<OvenState, OvenEvent>) context.getBean("ovenStateMachine"));
        connection.setApplicationContext(context);
        connection.setLog(LoggerFactory.getLogger(TFConnection.class));
        Assert.assertNotNull(connection);
    }

    private void mockPlugs() throws Exception {
        doNothing().when(ovenPlug).initialize();
        doNothing().when(ovenPlug).sendState();
        doNothing().when(smokerPlug).initialize();
        doNothing().when(smokerPlug).sendState();
        doCallRealMethod().when(ovenPlug).turnOn();
        doCallRealMethod().when(ovenPlug).turnOff();
        doCallRealMethod().when(smokerPlug).turnOn();
        doCallRealMethod().when(smokerPlug).turnOff();
        doCallRealMethod().when(ovenPlug).getState();
        doCallRealMethod().when(smokerPlug).getState();
        doCallRealMethod().when(ovenPlug).stateEntered(Matchers.any(State.class));
        doCallRealMethod().when(smokerPlug).stateEntered(Matchers.any(State.class));
        doCallRealMethod().when(ovenPlug).setLog(Matchers.any(Logger.class));
        doCallRealMethod().when(smokerPlug).setLog(Matchers.any(Logger.class));
        ovenPlug.setLog(LoggerFactory.getLogger(OvenPlug.class));
        smokerPlug.setLog(LoggerFactory.getLogger(SmokerPlug.class));
        Assert.assertNotNull(ovenPlug);
        Assert.assertNotNull(smokerPlug);
        ovenStateMachine.addStateListener(ovenPlug);
        ovenStateMachine.addStateListener(smokerPlug);
        ovenPlug.turnOff();
        smokerPlug.turnOff();
    }

    private void mockTemperatureSensor() throws Exception {
        doNothing().when(roomTemperatureSensor).initialize();
        doNothing().when(objectTemperatureSensor).initialize();
        doCallRealMethod().when(roomTemperatureSensor).getTemperature();
        doCallRealMethod().when(roomTemperatureSensor).temperature(Matchers.anyInt());
        doCallRealMethod().when(objectTemperatureSensor).getTemperature();
        doCallRealMethod().when(objectTemperatureSensor).temperature(Matchers.anyInt());
        doCallRealMethod().when(roomTemperatureSensor).getTargetTemperature();
        doCallRealMethod().when(roomTemperatureSensor).setTargetTemperature(Matchers.anyDouble());
        doCallRealMethod().when(objectTemperatureSensor).getTargetTemperature();
        doCallRealMethod().when(objectTemperatureSensor).setTargetTemperature(Matchers.anyDouble());
        doCallRealMethod().when(roomTemperatureSensor).evaluate((StateContext<OvenState, OvenEvent>) Matchers.any());
        doCallRealMethod().when(roomTemperatureSensor).setLog(Matchers.any(Logger.class));
        doCallRealMethod().when(objectTemperatureSensor).setLog(Matchers.any(Logger.class));
        doCallRealMethod().when(roomTemperatureSensor).setMaxTemperature(Matchers.anyDouble());
        doCallRealMethod().when(objectTemperatureSensor).setMaxTemperature(Matchers.anyDouble());
        roomTemperatureSensor.setLog(LoggerFactory.getLogger(RoomTemperatureSensor.class));
        objectTemperatureSensor.setLog(LoggerFactory.getLogger(ObjectTemperatureSensor.class));
        roomTemperatureSensor.setMaxTemperature(150d);
        objectTemperatureSensor.setMaxTemperature(150d);

    }
}

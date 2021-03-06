package de.onesi.hoffnet.tinkerforge.io;

import com.tinkerforge.BrickletDualRelay;
import com.tinkerforge.NotConnectedException;
import com.tinkerforge.TimeoutException;
import de.onesi.hoffnet.events.OvenEvent;
import de.onesi.hoffnet.states.OvenState;
import de.onesi.hoffnet.tinkerforge.IComponent;
import de.onesi.hoffnet.tinkerforge.TFConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.listener.StateMachineListener;
import org.springframework.statemachine.state.State;
import org.springframework.statemachine.transition.Transition;

public class Plug implements IComponent, StateMachineListener<OvenState, OvenEvent> {

    protected Logger log;
    protected String uuid;
    protected short relayno;
    private TFConnection connection;
    private BrickletDualRelay relay;
    private boolean state = false;

    @Autowired
    public Plug(TFConnection connection) {
        this.connection = connection;
        log = LoggerFactory.getLogger(this.getClass());
    }

    public void setLog(Logger log) {
        this.log = log;
    }

    @Override
    public void initialize() throws Exception {
        relay = new BrickletDualRelay(uuid, connection);
        this.connection.getOvenStateMachine().addStateListener(this);
        turnOff();
    }

    @Override
    public short identfier() {
        return BrickletDualRelay.DEVICE_IDENTIFIER;
    }

    public String getUuid() {
        return uuid;
    }

    public void turnOn() throws TimeoutException, NotConnectedException {
        state = true;
        sendState();
        log.info("turned On");
    }

    public void turnOff() throws TimeoutException, NotConnectedException {
        state = false;
        sendState();
        log.info("turned Off");
    }

    public void sendState() throws TimeoutException, NotConnectedException {
        relay.setSelectedState(relayno, state);
    }

    public boolean getState() {
        return state;
    }

    @Override
    public void stateChanged(State<OvenState, OvenEvent> from, State<OvenState, OvenEvent> to) {

    }

    @Override
    public void stateEntered(State<OvenState, OvenEvent> state) {

    }

    @Override
    public void stateExited(State<OvenState, OvenEvent> state) {

    }

    @Override
    public void eventNotAccepted(Message<OvenEvent> event) {

    }

    @Override
    public void transition(Transition<OvenState, OvenEvent> transition) {

    }

    @Override
    public void transitionStarted(Transition<OvenState, OvenEvent> transition) {

    }

    @Override
    public void transitionEnded(Transition<OvenState, OvenEvent> transition) {

    }

    @Override
    public void stateMachineStarted(StateMachine<OvenState, OvenEvent> stateMachine) {

    }

    @Override
    public void stateMachineStopped(StateMachine<OvenState, OvenEvent> stateMachine) {

    }

    @Override
    public void stateMachineError(StateMachine<OvenState, OvenEvent> stateMachine, Exception exception) {

    }

    @Override
    public void extendedStateChanged(Object key, Object value) {

    }

    @Override
    public void stateContext(StateContext<OvenState, OvenEvent> stateContext) {

    }
}

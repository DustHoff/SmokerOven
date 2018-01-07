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

    protected Logger logger;
    protected String uuid;
    protected short relayno;
    @Autowired
    private TFConnection connection;
    private BrickletDualRelay relay;
    private boolean state = false;

    public Plug() {
        logger = LoggerFactory.getLogger(this.getClass());
    }

    @Override
    public void initialize() throws Exception {
        logger.info("UUID:"+uuid+", Connection:"+connection);
        relay = new BrickletDualRelay(uuid, connection);
        connection.getOvenStateMachine().addStateListener(this);

    }

    public void turnOn() throws TimeoutException, NotConnectedException {
        state = true;
        sendState();
        logger.info("turned On");
    }

    public void turnOff() throws TimeoutException, NotConnectedException {
        state = false;

        logger.info("turned Off");
    }

    public void sendState() throws TimeoutException, NotConnectedException {
        BrickletDualRelay.State state = relay.getState();
        switch (relayno) {
            case 1:
                relay.setState(this.state, state.relay2);
                break;
            case 2:
                relay.setState(state.relay1, this.state);
                break;
        }
    }

    public boolean getState() {
        return state;
    }

    public void setLogger(Logger logger) {
        this.logger = logger;
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

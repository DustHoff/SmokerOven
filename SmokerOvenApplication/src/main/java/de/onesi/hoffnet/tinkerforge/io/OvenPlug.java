package de.onesi.hoffnet.tinkerforge.io;

import de.onesi.hoffnet.events.OvenEvent;
import de.onesi.hoffnet.states.OvenState;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.Message;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.listener.StateMachineListener;
import org.springframework.statemachine.state.State;
import org.springframework.statemachine.transition.Transition;
import org.springframework.stereotype.Component;

@Component
public class OvenPlug extends Plug implements StateMachineListener<OvenState, OvenEvent> {

    @Value("${tf.io.plug.oven.id:uuid}")
    protected void setUuid(String uuid) {
        this.uuid = uuid;
    }

    @Value("${tf.io.plug.oven.relay:1}")
    protected void setRelayno(short relayno) {
        this.relayno = relayno;
    }

    public OvenPlug() {
        super();
    }

    @Override
    public void stateChanged(State<OvenState, OvenEvent> from, State<OvenState, OvenEvent> to) {

    }

    @Override
    public void stateEntered(State<OvenState, OvenEvent> to) {
        try {
            switch (to.getId()) {
                case HEATING:
                    turnOn();
                    break;
                case FINISHED:
                case COOLING:
                    turnOff();
                    break;
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    @Override
    public void stateExited(State<OvenState, OvenEvent> state) {

    }

    @Override
    public void eventNotAccepted(Message<OvenEvent> message) {

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
    public void stateMachineError(StateMachine<OvenState, OvenEvent> stateMachine, Exception e) {

    }

    @Override
    public void extendedStateChanged(Object o, Object o1) {

    }

    @Override
    public void stateContext(StateContext<OvenState, OvenEvent> stateContext) {

    }
}

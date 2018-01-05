package de.onesi.hoffnet.tinkerforge.io;

import de.onesi.hoffnet.events.SmokerEvent;
import de.onesi.hoffnet.states.SmokerState;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.Message;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.listener.StateMachineListener;
import org.springframework.statemachine.state.State;
import org.springframework.statemachine.transition.Transition;
import org.springframework.stereotype.Component;

@Component
public class SmokerPlug extends Plug implements StateMachineListener<SmokerState, SmokerEvent> {
    @Value("${tf.io.plug.smoker.id:uuid}")
    protected void setUuid(String uuid) {
        this.uuid = uuid;
    }

    @Value("${tf.io.plug.smoker.relay:2}")
    protected void setRelayno(short relayno) {
        this.relayno = relayno;
    }

    @Override
    public void stateChanged(State<SmokerState, SmokerEvent> state, State<SmokerState, SmokerEvent> state1) {

    }

    @Override
    public void stateEntered(State<SmokerState, SmokerEvent> state) {
        try {
            switch (state.getId()) {
                case SMOKE:
                    turnOn();
                    break;
                case FINISHED:
                case AIR:
                    turnOff();
                    break;

            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    @Override
    public void stateExited(State<SmokerState, SmokerEvent> state) {

    }

    @Override
    public void eventNotAccepted(Message<SmokerEvent> message) {

    }

    @Override
    public void transition(Transition<SmokerState, SmokerEvent> transition) {

    }

    @Override
    public void transitionStarted(Transition<SmokerState, SmokerEvent> transition) {

    }

    @Override
    public void transitionEnded(Transition<SmokerState, SmokerEvent> transition) {

    }

    @Override
    public void stateMachineStarted(StateMachine<SmokerState, SmokerEvent> stateMachine) {

    }

    @Override
    public void stateMachineStopped(StateMachine<SmokerState, SmokerEvent> stateMachine) {

    }

    @Override
    public void stateMachineError(StateMachine<SmokerState, SmokerEvent> stateMachine, Exception e) {

    }

    @Override
    public void extendedStateChanged(Object o, Object o1) {

    }

    @Override
    public void stateContext(StateContext<SmokerState, SmokerEvent> stateContext) {

    }
}

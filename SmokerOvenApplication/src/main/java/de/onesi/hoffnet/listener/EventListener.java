package de.onesi.hoffnet.listener;

import de.onesi.hoffnet.events.OvenEvent;
import de.onesi.hoffnet.states.OvenState;
import de.onesi.hoffnet.web.data.State;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.Message;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.listener.StateMachineListener;
import org.springframework.statemachine.transition.Transition;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.Queue;

@Component
public class EventListener implements StateMachineListener<OvenState, OvenEvent> {

    private Logger log = LoggerFactory.getLogger(this.getClass());
    private Queue<State> events = new LinkedList();

    public State getNextEvent() {
        if (events.isEmpty()) return null;
        return events.poll();
    }

    public void addEvent(State state) {
        events.add(state);
    }

    @Override
    public void stateChanged(org.springframework.statemachine.state.State from, org.springframework.statemachine.state.State to) {

    }

    @Override
    public void stateEntered(org.springframework.statemachine.state.State<OvenState, OvenEvent> state) {
        if (state.getId().equals(OvenState.FAILED)) return;
        if (state.getId().equals(OvenState.HEATING) || state.getId().equals(OvenState.COOLING)) return;
        events.add(new State(state.getId()));
    }

    @Override
    public void stateExited(org.springframework.statemachine.state.State state) {

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

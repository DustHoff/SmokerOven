package de.onesi.hoffnet.guard;

import de.onesi.hoffnet.events.OvenEvent;
import de.onesi.hoffnet.states.OvenState;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.guard.Guard;

public class HasTimeGuard implements Guard<OvenState, OvenEvent> {

    @Override
    public boolean evaluate(StateContext<OvenState, OvenEvent> context) {

        return context.getExtendedState().getVariables().containsKey("time");
    }
}

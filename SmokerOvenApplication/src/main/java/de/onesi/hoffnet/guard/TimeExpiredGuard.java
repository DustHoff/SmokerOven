package de.onesi.hoffnet.guard;

import de.onesi.hoffnet.events.OvenEvent;
import de.onesi.hoffnet.states.OvenState;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.guard.Guard;

import java.util.Date;

public class TimeExpiredGuard implements Guard<OvenState, OvenEvent> {
    @Override
    public boolean evaluate(StateContext<OvenState, OvenEvent> context) {
        Date start = context.getExtendedState().get("start", Date.class);
        return new Date().after(start);
    }
}

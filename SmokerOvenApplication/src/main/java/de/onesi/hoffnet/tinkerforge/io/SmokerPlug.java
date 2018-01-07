package de.onesi.hoffnet.tinkerforge.io;

import de.onesi.hoffnet.events.OvenEvent;
import de.onesi.hoffnet.states.OvenState;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.statemachine.listener.StateMachineListener;
import org.springframework.statemachine.state.State;
import org.springframework.stereotype.Component;

@Component
public class SmokerPlug extends Plug implements StateMachineListener<OvenState, OvenEvent> {
    @Value("${tf.io.plug.smoker.id:uuid}")
    protected void setUuid(String uuid) {
        this.uuid = uuid;
    }

    @Value("${tf.io.plug.smoker.relay:2}")
    protected void setRelayno(short relayno) {
        this.relayno = relayno;
    }

    @Override
    public void stateEntered(State<OvenState, OvenEvent> state) {
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
}

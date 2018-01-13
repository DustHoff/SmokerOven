package de.onesi.hoffnet.tinkerforge.io;

import de.onesi.hoffnet.events.OvenEvent;
import de.onesi.hoffnet.states.OvenState;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.statemachine.state.State;
import org.springframework.stereotype.Component;

@Component
public class OvenPlug extends Plug {

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
            log.error(e.getMessage(), e);
        }
    }
}

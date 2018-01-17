package de.onesi.hoffnet.tinkerforge.sensor;

import de.onesi.hoffnet.events.OvenEvent;
import de.onesi.hoffnet.states.OvenState;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.guard.Guard;
import org.springframework.stereotype.Component;

@Component
public class RoomTemperatureSensor extends TemperatureSensor implements Guard<OvenState, OvenEvent> {

    @Value("${tf.sensor.temperature.room:uuid}")
    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    @Override
    public boolean evaluate(StateContext<OvenState, OvenEvent> stateContext) {
        if(getTargetTemperature()==null) return false;
        switch (stateContext.getTarget().getId()){
            case COOLING:
                return ((getTemperature() + tolerance) > getTargetTemperature());
            case HEATING:
                return ((getTemperature() - tolerance) < getTargetTemperature());
        }
        return false;
    }
}

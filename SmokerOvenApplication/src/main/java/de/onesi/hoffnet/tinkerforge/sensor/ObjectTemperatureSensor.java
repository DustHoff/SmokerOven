package de.onesi.hoffnet.tinkerforge.sensor;

import de.onesi.hoffnet.events.OvenEvent;
import de.onesi.hoffnet.states.OvenState;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.statemachine.StateContext;
import org.springframework.stereotype.Component;

@Component
public class ObjectTemperatureSensor extends TemperatureSensor  {

    @Value("${tf.sensor.temperature.object:uuid}")
    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    @Override
    public void temperature(int temperature) {
        super.temperature(temperature);
        if(getTargetTemperature()==null)return;
        if((this.getTemperature()+tolerance)>=getTargetTemperature()){
            ovenStateMachine.sendEvent(OvenEvent.TEMPERATURE_REACHED);
        }
    }
}

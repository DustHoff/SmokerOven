package de.onesi.hoffnet.web;

import com.google.gson.Gson;
import de.onesi.hoffnet.events.OvenEvent;
import de.onesi.hoffnet.states.OvenState;
import de.onesi.hoffnet.tinkerforge.TFConnection;
import de.onesi.hoffnet.tinkerforge.sensor.ObjectTemperatureSensor;
import de.onesi.hoffnet.tinkerforge.sensor.RoomTemperatureSensor;
import de.onesi.hoffnet.web.data.Configuration;
import de.onesi.hoffnet.web.data.Temperature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.statemachine.StateMachine;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SystemApi implements ISystemAPI {

    private final Gson gson;
    @Autowired
    private RoomTemperatureSensor roomTemperatureSensor;
    @Autowired
    private ObjectTemperatureSensor objectTemperatureSensor;
    @Autowired
    private TFConnection connection;
    @Autowired
    private StateMachine<OvenState, OvenEvent> ovenStateMachine;
    private Configuration configuration = new Configuration();

    public SystemApi() {
        gson = new Gson();
    }

    @RequestMapping("/state")
    public String state() {
        return gson.toJson(ovenStateMachine.getState().getId());
    }

    @RequestMapping(value = "/configuration", method = {RequestMethod.POST, RequestMethod.GET})
    public String configure(@RequestBody Configuration configuration) {
        if (configuration != null) {
            this.configuration = configuration;
            roomTemperatureSensor.setTargetTemperature(configuration.getRoomTemperature());
            objectTemperatureSensor.setTargetTemperature(configuration.getObjectTemperature());
            roomTemperatureSensor.setTolerance(configuration.getTemperatureTolerance());
            objectTemperatureSensor.setTolerance(configuration.getTemperatureTolerance());
            ovenStateMachine.sendEvent(OvenEvent.CONFIGURED);
        }
        return gson.toJson(this.configuration);
    }

    @RequestMapping(value = "/temperature")
    public String temperature() {
        return gson.toJson(new Temperature(objectTemperatureSensor.getTemperature(), roomTemperatureSensor.getTargetTemperature()));
    }
}

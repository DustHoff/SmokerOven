package de.onesi.hoffnet.web;

import com.google.gson.Gson;
import de.onesi.hoffnet.events.OvenEvent;
import de.onesi.hoffnet.states.OvenState;
import de.onesi.hoffnet.tinkerforge.TFConnection;
import de.onesi.hoffnet.tinkerforge.sensor.ObjectTemperatureSensor;
import de.onesi.hoffnet.tinkerforge.sensor.RoomTemperatureSensor;
import de.onesi.hoffnet.web.data.Configuration;
import de.onesi.hoffnet.web.data.Temperature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.statemachine.StateMachine;
import org.springframework.web.bind.annotation.*;

@RestController
public class SystemApi {

    private Logger log = LoggerFactory.getLogger(SystemApi.class);
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

    @RequestMapping(value = "/state", produces = MediaType.APPLICATION_JSON_VALUE)
    public OvenState state() {
        return ovenStateMachine.getState().getId();
    }

    @PostMapping(value = "/configure", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Configuration configure(@RequestBody Configuration configuration) {
        roomTemperatureSensor.setTargetTemperature(configuration.getRoomTemperature());
        objectTemperatureSensor.setTargetTemperature(configuration.getObjectTemperature());
        roomTemperatureSensor.setTolerance(configuration.getTemperatureTolerance());
        objectTemperatureSensor.setTolerance(configuration.getTemperatureTolerance());
        configuration.setRoomTemperature(roomTemperatureSensor.getTargetTemperature());
        configuration.setObjectTemperature(objectTemperatureSensor.getTargetTemperature());
        if (configuration.getStartDate() != null)
            ovenStateMachine.getExtendedState().getVariables().put("start", configuration.getStartDate());
        this.configuration = configuration;
        ovenStateMachine.sendEvent(OvenEvent.CONFIGURED);
        return getConfiguration();
    }

    @GetMapping(value = "/configure")
    public Configuration getConfiguration() {
        return configuration;
    }

    @GetMapping(value = "/temperature", produces = MediaType.APPLICATION_JSON_VALUE)
    public Temperature temperature() {
        Temperature temperature = new Temperature();
        temperature.setObjectTemperature(objectTemperatureSensor.getTemperature());
        temperature.setRoomTemperature(roomTemperatureSensor.getTemperature());
        return temperature;
    }
}

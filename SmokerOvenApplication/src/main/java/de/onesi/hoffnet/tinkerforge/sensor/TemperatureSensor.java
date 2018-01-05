package de.onesi.hoffnet.tinkerforge.sensor;

import com.tinkerforge.BrickletThermocouple;
import de.onesi.hoffnet.events.OvenEvent;
import de.onesi.hoffnet.states.OvenState;
import de.onesi.hoffnet.tinkerforge.IComponent;
import de.onesi.hoffnet.tinkerforge.TFConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.guard.Guard;


public class TemperatureSensor implements IComponent, BrickletThermocouple.TemperatureListener  {
    protected Logger log = LoggerFactory.getLogger(this.getClass());
    protected String uuid;
    @Autowired
    protected StateMachine<OvenState,OvenEvent> ovenStateMachine;
    @Autowired
    private TFConnection connection;
    private BrickletThermocouple sensor;
    private double temperature = 0d;
    private Double targetTemperature;
    @Value("${tf.sensor.temperature.tolerance:2.0}")
    protected double tolerance;

    @Override
    public void initialize() throws Exception {
        sensor = new BrickletThermocouple(uuid, connection);
        sensor.addTemperatureListener(this);
        sensor.setDebouncePeriod(60000);
        ovenStateMachine = connection.getOvenStateMachine();
    }

    public double getTemperature() {
        return temperature;
    }

    @Override
    public void temperature(int temperature) {
        this.temperature = temperature / 100.d;
        ovenStateMachine.sendEvent(OvenEvent.TEMPERATURE_CHANGED);
        log.info("Changed Temperature to "+this.temperature);
    }


    public Double getTargetTemperature() {
        return targetTemperature;
    }

    public void setTargetTemperature(double targetTemperature) {
        this.targetTemperature = targetTemperature;
    }

    public double getTolerance() {
        return tolerance;
    }

    public void setTolerance(double tolerance) {
        this.tolerance = tolerance;
    }

    public void setOvenStateMachine(StateMachine<OvenState, OvenEvent> ovenStateMachine) {
        this.ovenStateMachine = ovenStateMachine;
    }

    public void setLog(Logger log) {
        this.log = log;
    }
}

package de.onesi.hoffnet.tinkerforge.sensor;

import com.tinkerforge.BrickletThermocouple;
import de.onesi.hoffnet.events.OvenEvent;
import de.onesi.hoffnet.tinkerforge.IComponent;
import de.onesi.hoffnet.tinkerforge.TFConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;


public class TemperatureSensor implements IComponent, BrickletThermocouple.TemperatureListener {
    protected Logger log;
    protected String uuid;
    @Autowired
    protected TFConnection connection;
    private BrickletThermocouple sensor;
    private double temperature = 0d;
    private Double targetTemperature;
    @Value("${tf.sensor.temperature.tolerance:2.0}")
    protected double tolerance;
    @Value("${tf.sensor.temperature.max}")
    protected double maxTemperature;

    public TemperatureSensor() {
        log = LoggerFactory.getLogger(this.getClass());
    }

    @Override
    public void initialize() throws Exception {
        sensor = new BrickletThermocouple(uuid, connection);
        sensor.addTemperatureListener(this);
        sensor.setResponseExpected(BrickletThermocouple.FUNCTION_SET_TEMPERATURE_CALLBACK_PERIOD, false);
        sensor.setTemperatureCallbackPeriod(1000);
    }

    public void setLog(Logger log) {
        this.log = log;
    }

    @Override
    public short identfier() {
        return BrickletThermocouple.DEVICE_IDENTIFIER;
    }

    public String getUuid() {
        return uuid;
    }

    public double getTemperature() {
        return temperature;
    }

    @Override
    public void temperature(int temperature) {
        this.temperature = temperature / 100.d;
        connection.getOvenStateMachine().sendEvent(OvenEvent.TEMPERATURE_CHANGED);
        log.info("Changed Temperature to " + this.temperature);
    }


    public Double getTargetTemperature() {
        return targetTemperature;
    }

    public void setTargetTemperature(double targetTemperature) {
        if (targetTemperature > maxTemperature) {
            this.targetTemperature = maxTemperature;
            return;
        }
        this.targetTemperature = targetTemperature;
    }

    public double getTolerance() {
        return tolerance;
    }

    public void setTolerance(double tolerance) {
        this.tolerance = tolerance;
    }

    public void setMaxTemperature(double max) {
        this.maxTemperature = max;
    }
}

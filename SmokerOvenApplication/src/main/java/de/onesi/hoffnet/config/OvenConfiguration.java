package de.onesi.hoffnet.config;

import de.onesi.hoffnet.events.OvenEvent;
import de.onesi.hoffnet.guard.HasTimeGuard;
import de.onesi.hoffnet.guard.TimeExpiredGuard;
import de.onesi.hoffnet.states.OvenState;
import de.onesi.hoffnet.tinkerforge.TFConnection;
import de.onesi.hoffnet.tinkerforge.io.OvenPlug;
import de.onesi.hoffnet.tinkerforge.io.SmokerPlug;
import de.onesi.hoffnet.tinkerforge.sensor.ObjectTemperatureSensor;
import de.onesi.hoffnet.tinkerforge.sensor.RoomTemperatureSensor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.statemachine.config.EnableStateMachine;
import org.springframework.statemachine.config.EnumStateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;

@Configuration
@EnableStateMachine(name = "ovenStateMachine")
@Import({RoomTemperatureSensor.class, ObjectTemperatureSensor.class})
public class OvenConfiguration extends EnumStateMachineConfigurerAdapter<OvenState, OvenEvent> {

    @Override
    public void configure(StateMachineConfigurationConfigurer<OvenState, OvenEvent> config) throws Exception {
        config.withConfiguration().machineId("SmokerOven");
    }

    @Override
    public void configure(StateMachineStateConfigurer<OvenState, OvenEvent> states) throws Exception {
        states.withStates()
                .initial(OvenState.INITIALIZE)
                .state(OvenState.INITIALIZE, getConnection())
                .state(OvenState.READY)
                .state(OvenState.FAILED)
                .choice(OvenState.PREPAIRE)
                .state(OvenState.PREPAIRE_WAIT)
                .state(OvenState.PREPAIRE_NOTHING)
                .fork(OvenState.START)
                .state(OvenState.BUSY)
                .end(OvenState.FINISHED)
                .and().withStates().parent(OvenState.BUSY)
                .initial(OvenState.HEATING)
                .state(OvenState.HEATING)
                .state(OvenState.COOLING)
                .and().withStates().parent(OvenState.BUSY)
                .initial(OvenState.SMOKE)
                .state(OvenState.SMOKE)
                .state(OvenState.AIR);
    }

    @Override
    public void configure(StateMachineTransitionConfigurer<OvenState, OvenEvent> transitions) throws Exception {
        transitions
                .withExternal().source(OvenState.INITIALIZE).target(OvenState.READY).event(OvenEvent.INITIALIZED)
                .and()
                .withExternal().source(OvenState.INITIALIZE).target(OvenState.FAILED).event(OvenEvent.FAILED)
                .and()
                .withExternal().source(OvenState.READY).target(OvenState.FAILED).event(OvenEvent.FAILED)
                .and()
                .withExternal().source(OvenState.FAILED).target(OvenState.INITIALIZE).timerOnce(30000)
                .and()
                .withExternal().source(OvenState.READY).target(OvenState.PREPAIRE).event(OvenEvent.CONFIGURED)
                .and().withChoice().source(OvenState.PREPAIRE)
                .first(OvenState.PREPAIRE_WAIT, getHasTimeGuard())
                .last(OvenState.PREPAIRE_NOTHING)
                .and()
                .withExternal().source(OvenState.PREPAIRE_NOTHING).target(OvenState.START)
                .and()
                .withExternal()
                .source(OvenState.PREPAIRE_WAIT).target(OvenState.START).timer(60000).guard(getTimeExpiredGuard())
                .and()
                .withFork().source(OvenState.START)
                .target(OvenState.SMOKE)
                .target(OvenState.HEATING)
                .and()
                .withExternal().source(OvenState.BUSY).target(OvenState.FINISHED).event(OvenEvent.TEMPERATURE_REACHED)
                .and()
                // Oven activity
                .withExternal().source(OvenState.COOLING)
                .guard(getRoomTemperatureSensor())
                .target(OvenState.HEATING).event(OvenEvent.TEMPERATURE_CHANGED)
                .and()
                .withExternal().source(OvenState.HEATING)
                .guard(getRoomTemperatureSensor())
                .target(OvenState.COOLING).event(OvenEvent.TEMPERATURE_CHANGED)
                .and()
                // Smoker activity
                .withExternal().source(OvenState.SMOKE).target(OvenState.AIR).timer(18000000)
                .and()
                .withExternal().source(OvenState.AIR).target(OvenState.FINISHED).timer(3600000)
                .and()
                .withExternal().source(OvenState.AIR).target(OvenState.SMOKE).event(OvenEvent.SMOKER_REFILLED)
                .and()
                .withExternal().source(OvenState.FINISHED).target(OvenState.READY);
    }

    @Bean(name = "roomTemperatureSensor")
    public RoomTemperatureSensor getRoomTemperatureSensor() {
        RoomTemperatureSensor sensor = new RoomTemperatureSensor();
        return sensor;
    }

    @Bean(name = "objectTemperatureSensor")
    public ObjectTemperatureSensor getObjectTemperatureSensor() {
        ObjectTemperatureSensor sensor = new ObjectTemperatureSensor();
        return sensor;
    }

    @Bean(name = "ovenPlug")
    public OvenPlug getOvenPlug() {
        OvenPlug plug = new OvenPlug();
        return plug;
    }

    @Bean(name = "hasTimeGuard")
    public HasTimeGuard getHasTimeGuard() {
        return new HasTimeGuard();
    }

    @Bean(name = "timeExpiredGuard")
    public TimeExpiredGuard getTimeExpiredGuard() {
        return new TimeExpiredGuard();
    }

    @Bean(name = "smokerPlug")
    public SmokerPlug getSmokerPlug() {
        SmokerPlug plug = new SmokerPlug();
        return plug;
    }

    @Bean(name = "TFConnection")
    public TFConnection getConnection() {
        return new TFConnection();
    }
}

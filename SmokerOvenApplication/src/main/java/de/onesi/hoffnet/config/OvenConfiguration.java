package de.onesi.hoffnet.config;

import de.onesi.hoffnet.events.OvenEvent;
import de.onesi.hoffnet.guard.HasTimeGuard;
import de.onesi.hoffnet.guard.TimeExpiredGuard;
import de.onesi.hoffnet.states.OvenState;
import de.onesi.hoffnet.tinkerforge.TFConnection;
import de.onesi.hoffnet.tinkerforge.sensor.RoomTemperatureSensor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.config.EnableStateMachine;
import org.springframework.statemachine.config.EnumStateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;

@Configuration
@EnableStateMachine(name = "ovenStateMachine")
public class OvenConfiguration extends EnumStateMachineConfigurerAdapter<OvenState, OvenEvent> {

    @Autowired
    private TFConnection connection;
    @Autowired
    private RoomTemperatureSensor roomTemperatureSensor;

    @Override
    public void configure(StateMachineConfigurationConfigurer<OvenState, OvenEvent> config) throws Exception {
        config.withConfiguration().machineId("SmokerOven");
    }

    @Override
    public void configure(StateMachineStateConfigurer<OvenState, OvenEvent> states) throws Exception {
        states.withStates()
                .initial(OvenState.INITIALIZE)
                .state(OvenState.INITIALIZE, connection)
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
                .first(OvenState.PREPAIRE_WAIT, new HasTimeGuard())
                .last(OvenState.PREPAIRE_NOTHING)
                .and()
                .withExternal().source(OvenState.PREPAIRE_NOTHING).target(OvenState.START)
                .and()
                .withExternal()
                .source(OvenState.PREPAIRE_WAIT).target(OvenState.START).timer(60000).guard(new TimeExpiredGuard())
                .and()
                .withFork().source(OvenState.START)
                .target(OvenState.SMOKE)
                .target(OvenState.HEATING)
                .and()
                .withExternal().source(OvenState.BUSY).target(OvenState.FINISHED).event(OvenEvent.TEMPERATURE_REACHED)
                .and()
                // Oven activity
                .withExternal().source(OvenState.COOLING)
                .guard(roomTemperatureSensor)
                .target(OvenState.HEATING).event(OvenEvent.TEMPERATURE_CHANGED)
                .and()
                .withExternal().source(OvenState.HEATING)
                .guard(roomTemperatureSensor)
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
}

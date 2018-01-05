package de.onesi.hoffnet.config;

import de.onesi.hoffnet.events.OvenEvent;
import de.onesi.hoffnet.states.OvenState;
import de.onesi.hoffnet.tinkerforge.io.OvenPlug;
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
        config.withConfiguration().autoStartup(true).listener(getOvenPlug());
    }

    @Override
    public void configure(StateMachineStateConfigurer<OvenState, OvenEvent> states) throws Exception {
        states.withStates()
                .initial(OvenState.INITIALIZE)
                .state(OvenState.INITIALIZE)
                .state(OvenState.READY)
                .state(OvenState.BUSY)
                .end(OvenState.FINISHED)
                .and().withStates().parent(OvenState.BUSY)
                .initial(OvenState.HEATING)
                .state(OvenState.HEATING)
                .state(OvenState.COOLING);
    }

    @Override
    public void configure(StateMachineTransitionConfigurer<OvenState, OvenEvent> transitions) throws Exception {
        transitions
                .withExternal().source(OvenState.INITIALIZE).target(OvenState.READY).event(OvenEvent.INITIALIZED)
                .and()
                .withExternal().source(OvenState.INITIALIZE).target(OvenState.FAILED).event(OvenEvent.FAILED)
                .and()
                .withExternal().source(OvenState.READY).target(OvenState.BUSY).event(OvenEvent.CONFIGURED)
                .and()
                .withExternal().source(OvenState.BUSY).target(OvenState.FINISHED).event(OvenEvent.TEMPERATURE_REACHED)
                .and()
                .withExternal().source(OvenState.COOLING)
                .guard(getRoomTemperatrueSensor())
                .target(OvenState.HEATING).event(OvenEvent.TEMPERATURE_CHANGED)
                .and()
                .withExternal().source(OvenState.HEATING)
                .guard(getRoomTemperatrueSensor())
                .target(OvenState.COOLING).event(OvenEvent.TEMPERATURE_CHANGED)
                .and()
                .withExternal().source(OvenState.BUSY).target(OvenState.READY).timer(1000);
    }

    @Bean(name = "roomTemperatureSensor")
    public RoomTemperatureSensor getRoomTemperatrueSensor() {
        return new RoomTemperatureSensor();
    }

    @Bean(name = "objectTemperatureSensor")
    public ObjectTemperatureSensor getObjectTemperatureSensor() {
        return new ObjectTemperatureSensor();
    }

    @Bean(name = "ovenPlug")
    public OvenPlug getOvenPlug(){
        return new OvenPlug();
    }
}

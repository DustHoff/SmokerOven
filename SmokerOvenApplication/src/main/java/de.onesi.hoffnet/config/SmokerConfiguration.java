package de.onesi.hoffnet.config;

import de.onesi.hoffnet.events.SmokerEvent;
import de.onesi.hoffnet.states.SmokerState;
import de.onesi.hoffnet.tinkerforge.io.SmokerPlug;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.config.EnableStateMachine;
import org.springframework.statemachine.config.EnumStateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;

@Configuration
@EnableStateMachine(name = "smokerStateMachine")
public class SmokerConfiguration extends EnumStateMachineConfigurerAdapter<SmokerState, SmokerEvent> {
    @Override
    public void configure(StateMachineConfigurationConfigurer<SmokerState, SmokerEvent> config) throws Exception {
        config.withConfiguration().autoStartup(true).listener(getSmokerPlug());
    }

    @Override
    public void configure(StateMachineStateConfigurer<SmokerState, SmokerEvent> states) throws Exception {
        states.withStates().initial(SmokerState.READY)
                .state(SmokerState.READY)
                .state(SmokerState.BUSY)
                .state(SmokerState.FINISHED)
                .and().withStates()
                .parent(SmokerState.BUSY)
                .initial(SmokerState.SMOKE)
                .state(SmokerState.SMOKE)
                .state(SmokerState.AIR);
    }

    @Override
    public void configure(StateMachineTransitionConfigurer<SmokerState, SmokerEvent> transitions) throws Exception {
        transitions
                .withExternal().source(SmokerState.READY).target(SmokerState.BUSY).event(SmokerEvent.CONFIGURED)
                .and()
                .withExternal().source(SmokerState.AIR).target(SmokerState.SMOKE).event(SmokerEvent.REFILLED)
                .and()
                //Rauchmehl räuchert 4 bis 8 Stunden
                .withExternal().source(SmokerState.SMOKE).target(SmokerState.AIR).timer(18000000)
                .and()
                //Wenn nach 1 Stunde nicht nachgefüllt wird schaltet sich der Smoker ab
                .withExternal().source(SmokerState.AIR).target(SmokerState.FINISHED).timer(3600000);
    }

    @Bean(name = "smokerPlug")
    public SmokerPlug getSmokerPlug() {
        return new SmokerPlug();
    }
}

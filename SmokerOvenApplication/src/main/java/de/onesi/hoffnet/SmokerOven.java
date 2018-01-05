package de.onesi.hoffnet;

import de.onesi.hoffnet.events.OvenEvent;
import de.onesi.hoffnet.events.SmokerEvent;
import de.onesi.hoffnet.states.OvenState;
import de.onesi.hoffnet.states.SmokerState;
import de.onesi.hoffnet.tinkerforge.TFConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.messaging.Message;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.listener.StateMachineListener;
import org.springframework.statemachine.state.State;
import org.springframework.statemachine.transition.Transition;

@SpringBootApplication(scanBasePackages = "de.onesi.hoffnet")
public class SmokerOven implements CommandLineRunner {
    private final Logger log = LoggerFactory.getLogger(SmokerOven.class);

    public StateMachine<OvenState, OvenEvent> getOvenStateMachine() {
        return ovenStateMachine;
    }

    public StateMachine<SmokerState, SmokerEvent> getSmokerStateMachine() {
        return smokerStateMachine;
    }

    @Autowired
    private StateMachine<OvenState, OvenEvent> ovenStateMachine;
    @Autowired
    private StateMachine<SmokerState, SmokerEvent> smokerStateMachine;
    @Autowired
    private TFConnection connection;

    public static void main(String[] args) {
        SpringApplication.run(SmokerOven.class, args);
    }

    @Override
    public void run(String... strings) throws Exception {
        connection.connect();
    }
}

package de.onesi.hoffnet;

import de.onesi.hoffnet.events.OvenEvent;
import de.onesi.hoffnet.states.OvenState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.statemachine.StateMachine;

@SpringBootApplication(scanBasePackages = "de.onesi.hoffnet")
public class SmokerOven implements CommandLineRunner {

    @Autowired
    protected StateMachine<OvenState, OvenEvent> ovenStateMachine;

    public static void main(String[] args) {
        SpringApplication.run(SmokerOven.class, args);
    }

    @Override
    public void run(String... strings) throws Exception {
        ovenStateMachine.start();
    }
}

package de.onesi.hoffnet.tinkerforge;

import com.tinkerforge.AlreadyConnectedException;
import com.tinkerforge.IPConnection;
import com.tinkerforge.NetworkException;
import de.onesi.hoffnet.events.OvenEvent;
import de.onesi.hoffnet.states.ConnectionState;
import de.onesi.hoffnet.states.OvenState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.action.Action;
import org.springframework.stereotype.Component;

@Component
public class TFConnection extends IPConnection implements IPConnection.ConnectedListener, ApplicationContextAware, Action<OvenState, OvenEvent> {

    private Logger log;
    @Value("${tf.connection.host:127.0.0.1}")
    private String host;
    @Value("${tf.connection.port:4223}")
    private int port;
    @Value("${tf.connection.timeout:30000}")
    private int timeout;
    private ApplicationContext context;

    public TFConnection() {
        super();
        this.setAutoReconnect(true);
        this.setTimeout(timeout);
        this.addConnectedListener(this);
        log = LoggerFactory.getLogger(TFConnection.class);
    }

    public void connect() throws NetworkException, AlreadyConnectedException {
        if (this.getConnectionState() != 0) return;
        this.connect(host, port);
    }

    @Override
    public void connected(short state) {
        ConnectionState connectionState = ConnectionState.getStateByID(state);
        if (connectionState.equals(ConnectionState.CONNECTED)) {
            for (IComponent component: context.getBeansOfType(IComponent.class).values()) {
                try {
                    log.info("Initialize "+component.getClass().getSimpleName());
                    component.initialize();
                } catch (Exception e) {
                    e.printStackTrace();
                    getOvenStateMachine().sendEvent(OvenEvent.FAILED);
                }
            }
            getOvenStateMachine().sendEvent(OvenEvent.INITIALIZED);
        }
        log.info("Tinkerforge Connection changed to STATE " + connectionState.name());
    }

    public ConnectionState getState() {
        return ConnectionState.getStateByID(this.getConnectionState());
    }

    @Override
    public short getConnectionState() {
        return super.getConnectionState();
    }

    public StateMachine<OvenState, OvenEvent> getOvenStateMachine(){
        return (StateMachine<OvenState, OvenEvent>) context.getBean("ovenStateMachine");
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context=applicationContext;
    }

    public void setLog(Logger log) {
        this.log = log;
    }

    @Override
    public void execute(StateContext<OvenState, OvenEvent> context) {
        try {
            connect();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }
}

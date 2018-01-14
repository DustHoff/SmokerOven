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
public class TFConnection extends IPConnection implements IPConnection.ConnectedListener, IPConnection.DisconnectedListener, ApplicationContextAware, Action<OvenState, OvenEvent>, IPConnection.EnumerateListener {

    private Logger log;
    @Value("${tf.connection.host:127.0.0.1}")
    private String host;
    @Value("${tf.connection.port:4223}")
    private int port;
    @Value("${tf.connection.timeout:2500}")
    private int timeout;
    private ApplicationContext context;

    public TFConnection() {
        super();
        this.setAutoReconnect(true);
        this.setTimeout(timeout);
        this.addConnectedListener(this);
        this.addDisconnectedListener(this);
        this.addEnumerateListener(this);
        log = LoggerFactory.getLogger(TFConnection.class);
    }

    public void connect() throws NetworkException, AlreadyConnectedException {
        if (this.getConnectionState() != 0) return;
        this.connect(host, port);
    }

    @Override
    public void connected(short reason) {
        log.info("Tinkerforge Connection changed to STATE " + getState().name());
    }

    @Override
    public void disconnected(short disconnectReason) {
        log.info("Tinkerforge Connection changed to STATE " + getState().name());
    }

    public ConnectionState getState() {
        return ConnectionState.getStateByID(this.getConnectionState());
    }

    @Override
    public short getConnectionState() {
        return super.getConnectionState();
    }

    public StateMachine<OvenState, OvenEvent> getOvenStateMachine() {
        return (StateMachine<OvenState, OvenEvent>) context.getBean("ovenStateMachine");
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }

    public void setLog(Logger log) {
        this.log = log;
    }

    @Override
    public void execute(StateContext<OvenState, OvenEvent> context) {
        try {
            connect();
            enumerate();
            getOvenStateMachine().sendEvent(OvenEvent.INITIALIZED);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            getOvenStateMachine().sendEvent(OvenEvent.FAILED);
        }
    }

    @Override
    public int getTimeout() {
        return timeout;
    }

    public void waitForConnection() throws InterruptedException {
        while (!getState().equals(ConnectionState.CONNECTED)) {
            Thread.sleep(500);
        }
    }

    @Override
    public void enumerate(String uid, String connectedUid, char position, short[] hardwareVersion, short[] firmwareVersion, int deviceIdentifier, short enumerationType) {
        if (enumerationType == IPConnection.ENUMERATION_TYPE_DISCONNECTED) return;

        for (IComponent component : this.context.getBeansOfType(IComponent.class).values()) {
            if (deviceIdentifier == component.identfier() && uid.equals(component.getUuid())) {
                try {
                    log.info("Initialize " + component.getClass().getSimpleName() + " with UID " + uid + " on slot " + position + " Firmware " + firmwareVersion[0] + "." + firmwareVersion[1] + "." + firmwareVersion[2] + "v");
                    component.initialize();
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                    getOvenStateMachine().sendEvent(OvenEvent.FAILED);
                }
            }
        }

    }
}

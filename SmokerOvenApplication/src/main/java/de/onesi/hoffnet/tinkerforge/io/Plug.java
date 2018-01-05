package de.onesi.hoffnet.tinkerforge.io;

import com.tinkerforge.BrickletDualRelay;
import com.tinkerforge.NotConnectedException;
import com.tinkerforge.TimeoutException;
import de.onesi.hoffnet.tinkerforge.IComponent;
import de.onesi.hoffnet.tinkerforge.TFConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class Plug implements IComponent {

    protected Logger logger;
    protected String uuid;
    protected short relayno;
    @Autowired
    private TFConnection connection;
    private BrickletDualRelay relay;
    private boolean state = false;

    public Plug() {
        logger = LoggerFactory.getLogger(this.getClass());
    }

    @Override
    public void initialize() throws Exception {
        logger.info("UUID:"+uuid+", Connection:"+connection);
        relay = new BrickletDualRelay(uuid, connection);
    }

    public void turnOn() throws TimeoutException, NotConnectedException {
        state = true;
        sendState();
        logger.info("turned On");
    }

    public void turnOff() throws TimeoutException, NotConnectedException {
        state = false;

        logger.info("turned Off");
    }

    public void sendState() throws TimeoutException, NotConnectedException {
        BrickletDualRelay.State state = relay.getState();
        switch (relayno) {
            case 1:
                relay.setState(this.state, state.relay2);
                break;
            case 2:
                relay.setState(state.relay1, this.state);
                break;
        }
    }

    public boolean getState() {
        return state;
    }

    public void setLogger(Logger logger) {
        this.logger = logger;
    }
}

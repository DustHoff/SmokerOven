package de.onesi.hoffnet.web.data;

import de.onesi.hoffnet.states.ConnectionState;
import de.onesi.hoffnet.states.OvenState;
import de.onesi.hoffnet.states.SmokerState;

public class State {
    private ConnectionState connectionState;
    private OvenState ovenState;
    private SmokerState smokerState;

    public State() {
    }

    public State(ConnectionState connectionState, OvenState ovenState, SmokerState smokerState) {
        this.connectionState = connectionState;
        this.ovenState = ovenState;
        this.smokerState = smokerState;
    }

    public ConnectionState getConnectionState() {
        return connectionState;
    }

    public OvenState getOvenState() {
        return ovenState;
    }

    public SmokerState getSmokerState() {
        return smokerState;
    }

    public void setConnectionState(ConnectionState connectionState) {
        this.connectionState = connectionState;
    }

    public void setOvenState(OvenState ovenState) {
        this.ovenState = ovenState;
    }

    public void setSmokerState(SmokerState smokerState) {
        this.smokerState = smokerState;
    }
}

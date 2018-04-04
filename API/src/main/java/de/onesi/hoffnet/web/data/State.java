package de.onesi.hoffnet.web.data;

import java.util.Date;

import de.onesi.hoffnet.states.OvenState;

public final class State {

    private Date timestamp;
    private OvenState ovenState;
    private String message;

    private State() {
        // this is needed for Jackson to work
    }

    public State(OvenState ovenState) {
        this(ovenState, new Date(), null);
    }

    public State(OvenState ovenState, String message) {
        this(ovenState, new Date(), message);
    }

    public State(OvenState ovenState, Date timestamp, String message) {
        this.timestamp = new Date((timestamp.getTime()));
        this.ovenState = ovenState;
        this.message = message;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public OvenState getOvenState() {
        return ovenState;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "State{" +
                "timestamp=" + timestamp +
                ", ovenState=" + ovenState +
                ", message='" + message + '\'' +
                '}';
    }
}

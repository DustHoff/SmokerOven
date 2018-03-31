package de.onesi.hoffnet.web.data;

import java.util.Date;

import de.onesi.hoffnet.states.OvenState;

public class State {

    private Date timestamp;
    private OvenState ovenState;
    private String message;

    public State() {
    }

    public State(OvenState ovenState) {
        this.ovenState = ovenState;
        this.timestamp = new Date();
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public OvenState getOvenState() {
        return ovenState;
    }

    public void setOvenState(OvenState ovenState) {
        this.ovenState = ovenState;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
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

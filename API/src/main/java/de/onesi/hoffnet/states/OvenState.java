package de.onesi.hoffnet.states;

public enum  OvenState {
    INITIALIZE,READY,FAILED,BUSY,FINISHED,HEATING,COOLING;

    @Override
    public String toString() {
        return name();
    }
}

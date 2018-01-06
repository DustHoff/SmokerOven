package de.onesi.hoffnet.states;

public enum  OvenState {
    INITIALIZE, READY, FAILED, PREPAIRE, PREPAIRE_NOTHING, PREPAIRE_WAIT, START, BUSY, FINISHED, HEATING, COOLING, SMOKE, AIR;

    @Override
    public String toString() {
        return name();
    }
}

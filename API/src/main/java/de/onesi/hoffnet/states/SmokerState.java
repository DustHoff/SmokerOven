package de.onesi.hoffnet.states;

public enum SmokerState {
    READY,BUSY,SMOKE,AIR,FINISHED;


    @Override
    public String toString() {
        return name();
    }
}

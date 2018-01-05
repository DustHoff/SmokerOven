package de.onesi.hoffnet.states;

public enum ConnectionState {
    DISCONNECTED((short)0),CONNECTED((short)1),PENDING((short)2);

    private final short id;

    ConnectionState(short id) {
        this.id=id;
    }

    public static ConnectionState getStateByID(short id){
        switch (id){
            case 1:return CONNECTED;
            case 2:return PENDING;
        }
        return DISCONNECTED;
    }

    public short getId() {
        return id;
    }
}

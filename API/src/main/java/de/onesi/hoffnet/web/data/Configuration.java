package de.onesi.hoffnet.web.data;

public class Configuration {

    private Double temepratureTolerance;
    private Double roomTemperature;
    private Double objectTemperature;
    private boolean smoker;

    public Double getTemepratureTolerance() {
        return temepratureTolerance;
    }

    public void setTemepratureTolerance(Double temepratureTolerance) {
        this.temepratureTolerance = temepratureTolerance;
    }

    public Double getRoomTemperature() {
        return roomTemperature;
    }

    public void setRoomTemperature(Double roomTemperature) {
        this.roomTemperature = roomTemperature;
    }

    public Double getObjectTemperature() {
        return objectTemperature;
    }

    public void setObjectTemperature(Double objectTemperature) {
        this.objectTemperature = objectTemperature;
    }

    public boolean useSmoker() {
        return smoker;
    }

    public void setSmoker(boolean smoker) {
        this.smoker = smoker;
    }
}

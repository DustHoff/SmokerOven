package de.onesi.hoffnet.web.data;

public class Configuration {

    private Double temperatureTolerance;
    private Double roomTemperature;
    private Double objectTemperature;
    private boolean smoker;

    public Double getTemperatureTolerance() {
        return temperatureTolerance;
    }

    public void setTemperatureTolerance(Double temepratureTolerance) {
        this.temperatureTolerance = temepratureTolerance;
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

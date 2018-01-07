package de.onesi.hoffnet.web.data;

import java.util.Date;

public class Configuration {

    private Double temperatureTolerance;
    private Double roomTemperature;
    private Double objectTemperature;
    private Date startDate;

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

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }
}

package de.onesi.hoffnet.web.data;

import java.util.Date;

public class Configuration {

    private Double temepratureTolerance;
    private Double roomTemperature;
    private Double objectTemperature;
    private Date startDate;

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

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }
}

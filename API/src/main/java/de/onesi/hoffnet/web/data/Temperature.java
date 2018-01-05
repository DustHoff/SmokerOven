package de.onesi.hoffnet.web.data;

public class Temperature {

    private double objectTemperature;
    private double roomTemeprature;

    public Temperature(double objectTemperature, double roomTemeprature) {
        this.objectTemperature = objectTemperature;
        this.roomTemeprature = roomTemeprature;
    }

    public Temperature(){}

    public double getObjectTemperature() {
        return objectTemperature;
    }

    public void setObjectTemperature(double objectTemperature) {
        this.objectTemperature = objectTemperature;
    }

    public double getRoomTemeprature() {
        return roomTemeprature;
    }

    public void setRoomTemeprature(double roomTemeprature) {
        this.roomTemeprature = roomTemeprature;
    }
}

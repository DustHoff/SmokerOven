package de.onesi.hoffnet.web.data;

public class Temperature {

    private double objectTemperature;
    private double roomTemperature;

    public Temperature(double objectTemperature, double roomTemperature) {
        this.objectTemperature = objectTemperature;
        this.roomTemperature = roomTemperature;
    }

    public Temperature(){}

    public double getObjectTemperature() {
        return objectTemperature;
    }

    public void setObjectTemperature(double objectTemperature) {
        this.objectTemperature = objectTemperature;
    }

    public double getRoomTemperature() {
        return roomTemperature;
    }

    public void setRoomTemperature(double roomTemperature) {
        this.roomTemperature = roomTemperature;
    }
}

package com.example.prayertimeapp;

public class QiblaData {
    private double latitude;

    private double longitude;

    private double direction;

    public void setLatitude(double latitude){
        this.latitude = latitude;
    }
    public double getLatitude(){
        return this.latitude;
    }
    public void setLongitude(double longitude){
        this.longitude = longitude;
    }
    public double getLongitude(){
        return this.longitude;
    }
    public void setDirection(double direction){
        this.direction = direction;
    }
    public double getDirection(){
        return this.direction;
    }
}

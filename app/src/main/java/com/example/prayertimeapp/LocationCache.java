package com.example.prayertimeapp;

public class LocationCache {

    private Double latitude;
    private Double longitude;

    private static LocationCache instance;

    private LocationCache() {
    }

    public static LocationCache getInstance() {
        if (instance == null)
            instance = new LocationCache();
        return instance;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
}

package com.example.prayertimeapp;

public class Method {
    private int id;

    private String name;

    private Params params;

    private Location location;

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setParams(Params params) {
        this.params = params;
    }

    public Params getParams() {
        return this.params;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Location getLocation() {
        return this.location;
    }
}

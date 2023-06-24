package com.example.prayertimeapp;

public class QiblaResponse {
    private int code;
    private String status;

    private QiblaData data;

    public void setCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return this.code;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return this.status;
    }

    public void setData(QiblaData data) {
        this.data = data;
    }

    public QiblaData getData() {
        return this.data;
    }
}

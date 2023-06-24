package com.example.prayertimeapp;

public class Date {
    private String readable;

    private String timestamp;

    private Gregorian gregorian;

    private Hijri hijri;

    public void setReadable(String readable){
        this.readable = readable;
    }
    public String getReadable(){
        return this.readable;
    }
    public void setTimestamp(String timestamp){
        this.timestamp = timestamp;
    }
    public String getTimestamp(){
        return this.timestamp;
    }
    public void setGregorian(Gregorian gregorian){
        this.gregorian = gregorian;
    }
    public Gregorian getGregorian(){
        return this.gregorian;
    }
    public void setHijri(Hijri hijri){
        this.hijri = hijri;
    }
    public Hijri getHijri(){
        return this.hijri;
    }
}

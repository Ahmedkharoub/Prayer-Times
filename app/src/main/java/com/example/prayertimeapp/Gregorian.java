package com.example.prayertimeapp;

public class Gregorian {
    private String date;

    private String format;

    private String day;

    private GregorianWeekday Weekday;

    private GregorianMonth Month;

    private String year;

    private GregorianDesignation Designation;

    public void setDate(String date) {
        this.date = date;
    }

    public String getDate() {
        return this.date;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getFormat() {
        return this.format;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getDay() {
        return this.day;

    }


    public GregorianWeekday getWeekday() {
        return Weekday;
    }

    public void setWeekday(GregorianWeekday weekday) {
        Weekday = weekday;
    }

    public GregorianMonth getMonth() {
        return Month;
    }

    public void setMonth(GregorianMonth month) {
        Month = month;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public GregorianDesignation getDesignation() {
        return Designation;
    }

    public void setDesignation(GregorianDesignation designation) {
        Designation = designation;
    }

}

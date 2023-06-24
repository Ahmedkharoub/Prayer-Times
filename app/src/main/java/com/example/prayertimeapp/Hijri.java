package com.example.prayertimeapp;

import java.util.List;

public class Hijri {
    private String date;

    private String format;

    private String day;

    private HijriWeekday weekday;

    private HijriMonth month;

    private String year;

    private HijriDesignation designation;

    private List<String> holidays;

    public void setDate(String date){
        this.date = date;
    }
    public String getDate(){
        return this.date;
    }
    public void setFormat(String format){
        this.format = format;
    }
    public String getFormat(){
        return this.format;
    }
    public void setDay(String day){
        this.day = day;
    }
    public String getDay(){
        return this.day;
    }
    public void setWeekday(HijriWeekday hijriWeekday){
        this.weekday = hijriWeekday;
    }
    public HijriWeekday getWeekday(){
        return this.weekday;
    }
    public void setMonth(HijriMonth hijriMonth){
        this.month = hijriMonth;
    }
    public HijriMonth getMonth(){
        return this.month;
    }
    public void setYear(String year){
        this.year = year;
    }
    public String getYear(){
        return this.year;
    }
    public void setDesignation(HijriDesignation hijriDesignation){
        this.designation = hijriDesignation;
    }
    public HijriDesignation getDesignation(){
        return this.designation;
    }
    public void setHolidays(List<String> holidays){
        this.holidays = holidays;
    }
    public List<String> getHolidays(){
        return this.holidays;
    }
}

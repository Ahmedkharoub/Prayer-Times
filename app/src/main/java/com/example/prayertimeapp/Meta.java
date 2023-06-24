package com.example.prayertimeapp;

public class Meta {

        private double latitude;

        private double longitude;

        private String timezone;

        private Method method;

        private String latitudeAdjustmentMethod;

        private String midnightMode;

        private String school;

        private Offset offset;

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
        public void setTimezone(String timezone){
        this.timezone = timezone;
    }
        public String getTimezone(){
        return this.timezone;
    }
        public void setMethod(Method method){
        this.method = method;
    }
        public Method getMethod(){
        return this.method;
    }
        public void setLatitudeAdjustmentMethod(String latitudeAdjustmentMethod){
        this.latitudeAdjustmentMethod = latitudeAdjustmentMethod;
    }
        public String getLatitudeAdjustmentMethod(){
        return this.latitudeAdjustmentMethod;
    }
        public void setMidnightMode(String midnightMode){
        this.midnightMode = midnightMode;
    }
        public String getMidnightMode(){
        return this.midnightMode;
    }
        public void setSchool(String school){
        this.school = school;
    }
        public String getSchool(){
        return this.school;
    }
        public void setOffset(Offset offset){
        this.offset = offset;
    }
        public Offset getOffset(){
        return this.offset;
    }
}

package com.example.prayertimeapp;

public class Data {

        private Timings timings;

        private Date date;

        private Meta meta;

        public void setTimings(Timings timings){
            this.timings = timings;
        }
        public Timings getTimings(){
            return this.timings;
        }
        public void setDate(Date date){
            this.date = date;
        }
        public Date getDate(){
            return this.date;
        }
        public void setMeta(Meta meta){
            this.meta = meta;
        }
        public Meta getMeta(){
            return this.meta;
        }
}

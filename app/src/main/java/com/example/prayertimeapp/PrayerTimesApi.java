package com.example.prayertimeapp;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface PrayerTimesApi {

    @GET("v1/calendar/{year}/{month}")
    Call<Root> getPrayerTimes(
            @Path("year") int year,
            @Path("month") int month,
            @Query("latitude") double latitude,
            @Query("longitude") double longitude,
            @Query("method") int method
    );

}

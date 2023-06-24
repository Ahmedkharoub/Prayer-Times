package com.example.prayertimeapp;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface QiblaServiceApi {
    @GET("v1/qibla/{latitude}/{longitude}")
    Call<QiblaResponse> getQiblaDirection(@Path("latitude") double latitude, @Path("longitude") double longitude);
}


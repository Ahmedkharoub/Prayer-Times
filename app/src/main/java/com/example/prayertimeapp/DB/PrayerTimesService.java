package com.example.prayertimeapp.DB;

import android.app.MediaRouteButton;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.prayertimeapp.Data;
import com.example.prayertimeapp.PrayerTimesApi;
import com.example.prayertimeapp.Root;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PrayerTimesService {
    private static final String BASE_URL = "https://api.aladhan.com/";
    private static PrayerTimesService instance;
    private PrayerTimesApi prayerTimesApi;
    private Context mContext;
    private static final int METHOD = 5;
    private PrayerTimesDatabaseHelper prayerTimesDatabaseHelper;
    private Date lastFetch = new Date();
    ProgressBar progressBarDetails;
    ConstraintLayout coordinatorLayout;

    private PrayerTimesService() {

    }

    private PrayerTimesService(Context context) {
        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        clientBuilder.addInterceptor(loggingInterceptor);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(clientBuilder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        prayerTimesApi = retrofit.create(PrayerTimesApi.class);
        this.mContext = context;
        prayerTimesDatabaseHelper = new PrayerTimesDatabaseHelper(mContext);
    }

    public static PrayerTimesService getInstance(Context context) {
        if (instance == null) {
            instance = new PrayerTimesService(context);
        }
        return instance;
    }

    public void getPrayerTimes(int year, int month, int day, double latitude, double longitude, final OnPrayerTimesResponseListener listener) {
        Data prayerTimes = prayerTimesDatabaseHelper.getPrayerTimes(getPrimaryKey(year, month, day));
        if (prayerTimes != null) {
            listener.onPrayerTimesResponse(prayerTimes);
        }

        // if 1 minute has not passed or no local database
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(lastFetch);
        calendar.add(Calendar.MINUTE, 1);
        if (prayerTimes != null && lastFetch.before(calendar.getTime()))
            return;

        if (isNetworkAvailable(mContext)) {
            Call<Root> call = prayerTimesApi.getPrayerTimes(year, month, latitude, longitude, METHOD);
            call.enqueue(new Callback<Root>() {
                @Override
                public void onResponse(Call<Root> call, Response<Root> response) {
                    if (response.isSuccessful()) {
                        lastFetch = new Date();
                        Root prayerTimesResponse = response.body();
                        List<Data> data = prayerTimesResponse.getData();

                        for (int i = 0; i < data.size(); i++) {
                            Data datum = data.get(i);
                            prayerTimesDatabaseHelper.savePrayerTimes(getPrimaryKey(year, month, i + 1), datum);
                        }

                        Data todayData = data.get(day - 1);
                        listener.onPrayerTimesResponse(todayData);
                    } else {
                        listener.onPrayerTimesError(new Exception(response.message()));
                    }
                }

                @Override
                public void onFailure(Call<Root> call, Throwable t) {
                    listener.onPrayerTimesError(t);
                }
            });
        } else {
            Toast.makeText(mContext, "API request failed", Toast.LENGTH_SHORT).show();
        }
    }

    public interface OnPrayerTimesResponseListener {
        void onPrayerTimesResponse(Data prayerTimes);

        void onPrayerTimesError(Throwable t);
    }

    public boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager != null) {
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            return activeNetworkInfo != null && activeNetworkInfo.isConnected();
        }
        return false;
    }

    private String getPrimaryKey(int year, int month, int day) {
        return year + "-" + month + "-" + day;
    }
}


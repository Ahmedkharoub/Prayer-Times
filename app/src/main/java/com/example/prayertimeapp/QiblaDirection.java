package com.example.prayertimeapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.Menu;
import android.view.MenuItem;


import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Polyline;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class QiblaDirection extends AppCompatActivity {

    private MapView mapView;
    private double KAABA_LATITUDE = 21.424025;
    private double KAABA_LONGITUDE = 39.824837;
    private double DIRECTION = 0.0;
    double latitudeValue;
    double longitudeValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qibla_direction);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.action_bar)));
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);

//        Intent intent = getIntent();
//        latitudeValue = intent.getDoubleExtra("latitude", 0.0);
//        longitudeValue = intent.getDoubleExtra("longitude", 0.0);

        latitudeValue = LocationCache.getInstance().getLatitude();
        longitudeValue = LocationCache.getInstance().getLatitude();

        List<GeoPoint> points = new ArrayList<>();
        points.add(new GeoPoint(latitudeValue, longitudeValue));
        points.add(new GeoPoint(KAABA_LATITUDE, KAABA_LONGITUDE));

        Configuration.getInstance().load(getApplicationContext(), getSharedPreferences("osmdroid", MODE_PRIVATE));
        mapView = findViewById(R.id.map);
        mapView.setTileSource(TileSourceFactory.MAPNIK);
        mapView.getController().setZoom(5);
        mapView.setBuiltInZoomControls(true);
        mapView.setMultiTouchControls(true);
        GeoPoint centerPoint = new GeoPoint(29.5074, 31.1278);
        mapView.getController().setCenter(centerPoint);
        Polyline line = new Polyline();
        line.setPoints(points);
        mapView.getOverlayManager().add(line);
        Retrofit retrofit2 = new Retrofit.Builder()
                .baseUrl("https://api.aladhan.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        QiblaServiceApi service = retrofit2.create(QiblaServiceApi.class);

        Call<QiblaResponse> call = service.getQiblaDirection(latitudeValue, longitudeValue);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<QiblaResponse> call, Response<QiblaResponse> response) {
                if (response.isSuccessful()) {
                    QiblaResponse qiblaResponse = response.body();
                    QiblaData data = qiblaResponse.getData();
                    DIRECTION = data.getDirection();

                    Marker marker1 = new Marker(mapView);
                    marker1.setOnMarkerClickListener((marker, mapView) -> false);
                    marker1.setPosition(new GeoPoint(latitudeValue, longitudeValue));
                    mapView.getOverlays().add(marker1);

                    Marker marker2 = new Marker(mapView);
                    marker2.setOnMarkerClickListener((marker, mapView) -> false);
                    marker2.setPosition(new GeoPoint(KAABA_LATITUDE, KAABA_LONGITUDE));
                    mapView.getOverlays().add(marker2);
                } else {
                }
            }

            @Override
            public void onFailure(Call<QiblaResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.compass,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.compass_menu){
           Intent compass_menu = new Intent(QiblaDirection.this,QiblaCompass.class);
            compass_menu.putExtra("latitude",latitudeValue );
            compass_menu.putExtra("longitude", longitudeValue);
            compass_menu.putExtra("DIRECTION", DIRECTION);
            startActivity(compass_menu);
        }
        return super.onOptionsItemSelected(item);
    }
}
package com.example.prayertimeapp;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class QiblaCompass extends AppCompatActivity {

    private final double KAABA_LATITUDE = 21.424025;
    private final double KAABA_LONGITUDE = 39.824837;
    QiblaCompassView qiblaCompassView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qibla_compass);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.action_bar)));
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        qiblaCompassView = findViewById(R.id.qibla_compass_view);

        Intent intent = getIntent();
        double latitudeValue = intent.getDoubleExtra("latitude", 0.0);
        double longitudeValue = intent.getDoubleExtra("longitude", 0.0);
        double direction = intent.getDoubleExtra("DIRECTION", 0.0);

        qiblaCompassView.setLatitude(latitudeValue);
        qiblaCompassView.setLongitude(longitudeValue);
        qiblaCompassView.setKaabaLatitude(KAABA_LATITUDE);
        qiblaCompassView.setKaabaLongitude(KAABA_LONGITUDE);
        qiblaCompassView.setKaabaDirection(direction);
        qiblaCompassView.startListeningForSensorEvents();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        qiblaCompassView.stopListeningForSensorEvents();
    }
}

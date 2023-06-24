package com.example.prayertimeapp;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.prayertimeapp.DB.PrayerTimesDatabaseHelper;
import com.example.prayertimeapp.DB.PrayerTimesService;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback {

    TextView mFajr;
    TextView mSunrise;
    TextView mDhuhr;
    TextView mAsr;
    TextView mMaghrib;
    TextView mIsha;
    TextView mReadable;
    TextView mLocation;
    TextView mNextPray;
    TextView mPrayName;
    TextView mTime;
    ImageView forward;
    ImageView back;
    Button qibladirection;
    boolean permissionAsked = false;
    ProgressBar progressBarDetails;

    ConstraintLayout constraintLayout;

    private int REQUEST_CODE = 100;
    FusedLocationProviderClient fusedLocationProviderClient;
    int DAY = 0, MONTH = 0, YEAR = 0;
    int Hour = 0, Mints = 0;

    private PrayerTimesService prayerTimesService;
    private PrayerTimesDatabaseHelper prayerTimesDatabaseHelper;

    private double LATITUDE, LONGITUDE;
    private String ADDRESS;
    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        prayerTimesService = PrayerTimesService.getInstance(this);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.action_bar)));
        mFajr = findViewById(R.id.fajr_text_view);
        mSunrise = findViewById(R.id.sunrise_text_view);
        mDhuhr = findViewById(R.id.dhuhr_text_view);
        mAsr = findViewById(R.id.asr_text_view);
        mMaghrib = findViewById(R.id.maghrib_text_view);
        mIsha = findViewById(R.id.isha_text_view);
        mReadable = findViewById(R.id.readabel_date);
        mLocation = findViewById(R.id.location);
        mPrayName = findViewById(R.id.pray_name);
        mTime = findViewById(R.id.next_pray_time);
        forward = findViewById(R.id.forward_button);
        back = findViewById(R.id.back_button);
        qibladirection = findViewById(R.id.qibla_direction);
        progressBarDetails = findViewById(R.id.progressBar);
        constraintLayout = findViewById(R.id.parent);


        progressBarDetails.setVisibility(View.VISIBLE);
        constraintLayout.setVisibility(View.GONE);



        qibladirection.setOnClickListener(v -> {
            LocationCache.getInstance().setLatitude(LATITUDE);
            LocationCache.getInstance().setLatitude(LONGITUDE);
            Intent i = new Intent(MainActivity.this, QiblaDirection.class);
//                i.putExtra("latitude", LATITUDE);
//                i.putExtra("longitude", LONGITUDE);
            startActivity(i);
        });


        forward.setOnClickListener(v -> {
            incrementDay(false);
            getData();
        });

        back.setOnClickListener(v -> {
            incrementDay(true);
            getData();
        });

        Calendar calendar = Calendar.getInstance();
        int currentDay = calendar.get(Calendar.DAY_OF_MONTH);
        int currentMonth = calendar.get(Calendar.MONTH) + 1;
        int currentYear = calendar.get(Calendar.YEAR);

        DAY = currentDay;
        MONTH = currentMonth;
        YEAR = currentYear;
    }

    @Override
    protected void onResume() {
        super.onResume();

        getLastLocation();
    }

    private void displayPrayerTimes(Data data) {
        if (data != null) {
            SimpleDateFormat inputFormat = new SimpleDateFormat("HH:mm", Locale.US);
            SimpleDateFormat outputFormat = new SimpleDateFormat("hh:mm a", Locale.US);
            progressBarDetails.setVisibility(View.GONE);
            constraintLayout.setVisibility(View.VISIBLE);

            try {
                Date fajrDate = parseDate(inputFormat.parse(data.getTimings().getFajr()));
                Date sunriseDate = parseDate(inputFormat.parse(data.getTimings().getSunrise()));
                Date dhuhrDate = parseDate(inputFormat.parse(data.getTimings().getDhuhr()));
                Date asrDate = parseDate(inputFormat.parse(data.getTimings().getAsr()));
                Date maghribDate = parseDate(inputFormat.parse(data.getTimings().getMaghrib()));
                Date ishaDate = parseDate(inputFormat.parse(data.getTimings().getIsha()));

                mFajr.setText(outputFormat.format(fajrDate));
                mSunrise.setText(outputFormat.format(sunriseDate));
                mDhuhr.setText(outputFormat.format(dhuhrDate));
                mAsr.setText(outputFormat.format(asrDate));
                mMaghrib.setText(outputFormat.format(maghribDate));
                mIsha.setText(outputFormat.format(ishaDate));

                Instant instant = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    instant = Instant.ofEpochSecond(Long.parseLong(data.getDate().getTimestamp()));
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd yyyy").withZone(ZoneId.systemDefault());
                    String formattedDate = formatter.format(instant);
                    mReadable.setText(formattedDate);
                }
                mLocation.setText(ADDRESS);

                Date date = new Date();
                Date nextPrayerDate;
                String nextPrayerName;
                if (date.before(fajrDate)) {
                    nextPrayerDate = fajrDate;
                    nextPrayerName = "Fair";
                } else if (date.before(sunriseDate)) {
                    nextPrayerDate = sunriseDate;
                    nextPrayerName = "Sunrise";
                } else if (date.before(dhuhrDate)) {
                    nextPrayerDate = dhuhrDate;
                    nextPrayerName = "Dhuhr";
                } else if (date.before(asrDate)) {
                    nextPrayerDate = asrDate;
                    nextPrayerName = "Asr";
                } else if (date.before(maghribDate)) {
                    nextPrayerDate = maghribDate;
                    nextPrayerName = "Maghrib";
                } else if (date.before(ishaDate)) {
                    nextPrayerDate = ishaDate;
                    nextPrayerName = "Isha";
                } else {
                    // after Isha and before midnight
                    nextPrayerDate = fajrDate;
                    nextPrayerName = "Fair";
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(nextPrayerDate);
                    calendar.add(Calendar.DAY_OF_MONTH, 1);
                    nextPrayerDate.setTime(calendar.getTime().getTime());
                }

                long diffInMillies = Math.abs(nextPrayerDate.getTime() - date.getTime());
                long diffInMinutes = TimeUnit.MILLISECONDS.toMinutes(diffInMillies);
                long hours = diffInMinutes / 60;
                long minutes = diffInMinutes % 60;
                String formattedDiff;
                if (hours == 0) {
                    formattedDiff = String.format("%dmin", minutes);
                } else {
                    formattedDiff = String.format("%dhr %dmin", hours, minutes);
                }
                mTime.setText(formattedDiff);
                mPrayName.setText(nextPrayerName);


            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        } else {
            // handle the null object reference error
        }
    }

    private Date parseDate(Date parse) {
        Date date = new Date();
        date.setHours(parse.getHours());
        date.setMinutes(parse.getMinutes());
        date.setSeconds(parse.getSeconds());
        return date;
    }

    private void incrementDay(boolean minus) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, YEAR);
        calendar.set(Calendar.MONTH, MONTH - 1);
        calendar.set(Calendar.DAY_OF_MONTH, DAY);

        calendar.add(Calendar.DAY_OF_MONTH, minus ? -1 : 1);

        YEAR = calendar.get(Calendar.YEAR);
        MONTH = calendar.get(Calendar.MONTH) + 1;
        DAY = calendar.get(Calendar.DAY_OF_MONTH);
    }

    private void getData() {

        System.out.printf("%s-%s-%s", YEAR, MONTH, DAY);
        prayerTimesService.getPrayerTimes(YEAR, MONTH, DAY, LATITUDE, LONGITUDE, new PrayerTimesService.OnPrayerTimesResponseListener() {
            @Override
            public void onPrayerTimesResponse(Data prayerTimes) {
                displayPrayerTimes(prayerTimes);
            }

            @Override
            public void onPrayerTimesError(Throwable t) {
                t.printStackTrace();
                Toast.makeText(MainActivity.this, "There is Connection Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void getLastLocation() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            LocationRequest locationRequest = LocationRequest.create();
            locationRequest.setInterval(20000);
            locationRequest.setFastestInterval(10000);
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            fusedLocationProviderClient.requestLocationUpdates(locationRequest, new LocationCallback() {
                @Override
                public void onLocationResult(@NonNull LocationResult locationResult) {
                    super.onLocationResult(locationResult);
                    Location location = locationResult.getLastLocation();
                    if (location != null) {
                        double newLatitude = location.getLatitude();
                        double newLongitude = location.getLongitude();
                        if (newLatitude == LATITUDE || newLongitude == LONGITUDE) return;
                        LATITUDE = newLatitude;
                        LONGITUDE = newLongitude;
                        Geocoder geocoder = new Geocoder(MainActivity.this, Locale.getDefault());
                        List<Address> addresses = null;
                        try {
                            addresses = geocoder.getFromLocation(newLatitude, newLongitude, 1);
                            Address foundAddress = addresses.get(0);
                            ADDRESS = foundAddress.getAdminArea() + "," + foundAddress.getCountryName() + "," + foundAddress.getSubAdminArea();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        getData();
                    }
                }
            }, null);
        } else {
            askPermission();
        }
    }

    public void askPermission() {
        if (!permissionAsked) {
            permissionAsked = true;
            ActivityCompat.requestPermissions((Activity) this, new String[]
                    {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation();
            } else {
                showLocationPermissionRationale();
            }
        }
    }

    private void showLocationPermissionRationale() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("This app requires access to your location to work.")
                .setTitle("Permission required")
                .setPositiveButton("Grant", (dialog, id) -> {
                    permissionAsked = false;
                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package", getPackageName(), null);
                    intent.setData(uri);
                    startActivity(intent);
                })
                .setNegativeButton("Exit", (dialog, which) -> finish())
                .setCancelable(false);
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}

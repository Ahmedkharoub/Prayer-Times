package com.example.prayertimeapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.AttributeSet;
import android.view.View;

public class QiblaCompassView extends View implements SensorEventListener {
    private Paint paint;
    private Path path;
    private float currentDegree = 0f;
    private double qiblaDirection = 0.0;
    private SensorManager sensorManager;
    private Sensor accelerometerSensor;
    private Sensor magnetometerSensor;
    private float[] gravity = new float[3];
    private float[] geomagnetic = new float[3];
    private float[] rotationMatrix = new float[9];
    private Bitmap mBitmap;

    public QiblaCompassView(Context context, AttributeSet attrs) {
        super(context, attrs);

        // Initialize the Paint object
        paint = new Paint();
        paint.setTextSize(32);
        paint.setColor(Color.WHITE);
        paint.setTextAlign(Paint.Align.CENTER);

        path = new Path();

        // Get a reference to the SensorManager
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);

        // Get a reference to the accelerometer and magnetometer sensors
        accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        magnetometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.compass);
        mBitmap = Bitmap.createScaledBitmap(mBitmap, mBitmap.getWidth() / 2, mBitmap.getHeight() / 2, true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Draw the compass
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(10);
        float direction = (float) (qiblaDirection - currentDegree);
        if (Math.abs(direction) < 5) {
            paint.setColor(Color.GREEN);
        } else {
            paint.setColor(Color.WHITE);
        }
        canvas.drawCircle(getWidth() / 2, getHeight() / 2, (getWidth() / 3) + 5, paint);

        // Draw the direction to the Qibla
        paint.setStrokeWidth(4);
        canvas.rotate(direction, getWidth() / 2, getHeight() / 2);
        canvas.drawBitmap(mBitmap, (getWidth() / 2) - (mBitmap.getWidth() / 2), (getHeight() / 2) - (mBitmap.getHeight() / 2), paint);

        canvas.save();
        canvas.restore();
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Do nothing
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            gravity = event.values.clone();
        } else if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            geomagnetic = event.values.clone();
        }

        if (gravity != null && geomagnetic != null) {
            boolean success = SensorManager.getRotationMatrix(rotationMatrix, null, gravity, geomagnetic);

            if (success) {
                float[] orientationAngles = new float[3];
                SensorManager.getOrientation(rotationMatrix, orientationAngles);

                currentDegree = (float) Math.toDegrees(orientationAngles[0]);
                invalidate();
            }
        }
    }

    public void startListeningForSensorEvents() {
        sensorManager = (SensorManager) getContext().getSystemService(Context.SENSOR_SERVICE);
        accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        magnetometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        sensorManager.registerListener(this, accelerometerSensor, SensorManager.SENSOR_DELAY_GAME);
        sensorManager.registerListener(this, magnetometerSensor, SensorManager.SENSOR_DELAY_GAME);
    }

    public void stopListeningForSensorEvents() {
        sensorManager.unregisterListener(this);
    }

    public void setKaabaDirection(double direction) {
        this.qiblaDirection = direction;
    }
}


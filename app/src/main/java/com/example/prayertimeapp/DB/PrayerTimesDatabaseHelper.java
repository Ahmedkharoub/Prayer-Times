package com.example.prayertimeapp.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.prayertimeapp.Data;
import com.google.gson.Gson;

public class PrayerTimesDatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "prayer_times.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "prayer_times";
    private static final String COLUMN_DATE = "date";


    private static final String COLUMN_PRAYER_TIMES = "prayer_times";

    public PrayerTimesDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableSql = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_DATE + " TEXT PRIMARY KEY, " +
                COLUMN_PRAYER_TIMES + " TEXT)";
        db.execSQL(createTableSql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public void savePrayerTimes(String date, Data prayerTimes) {
        Data previousData = getPrayerTimes(date);
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_DATE, date);
        values.put(COLUMN_PRAYER_TIMES, new Gson().toJson(prayerTimes));
        if (previousData == null) {
            db.insert(TABLE_NAME, null, values);
        } else {
            db.update(TABLE_NAME, values, COLUMN_DATE + " = ?", new String[]{date});
        }
        db.close();
    }

    public Data getPrayerTimes(String date) {
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT " + COLUMN_PRAYER_TIMES + " FROM " + TABLE_NAME +
                " WHERE " + COLUMN_DATE + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{date});
        Data prayerTimes = null;

        if (cursor.moveToFirst()) {
            String prayerTimesJson = cursor.getString(cursor.getColumnIndex(COLUMN_PRAYER_TIMES));
            prayerTimes = new Gson().fromJson(prayerTimesJson, Data.class);

        }

        cursor.close();
        db.close();
        return prayerTimes;
    }
}

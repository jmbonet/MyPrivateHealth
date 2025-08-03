package com.myprivatehealth.app;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "MyPrivateHealth.db";
    private static final int DATABASE_VERSION = 1;

    // Users table
    public static final String TABLE_USERS = "users";
    public static final String COLUMN_USER_ID = "id";
    public static final String COLUMN_USER_NAME = "name";
    public static final String COLUMN_USER_AGE = "age";
    public static final String COLUMN_USER_HEIGHT = "height";

    // Health data table
    public static final String TABLE_HEALTH_DATA = "health_data";
    public static final String COLUMN_HEALTH_ID = "id";
    public static final String COLUMN_HEALTH_USER_ID = "user_id";
    public static final String COLUMN_HEALTH_DATE = "date";
    public static final String COLUMN_HEALTH_SYSTOLIC = "systolic_pressure";
    public static final String COLUMN_HEALTH_DIASTOLIC = "diastolic_pressure";
    public static final String COLUMN_HEALTH_WEIGHT = "weight";
    public static final String COLUMN_HEALTH_FAT_PERCENTAGE = "fat_percentage";
    public static final String COLUMN_HEALTH_BMI = "bmi";

    // Create users table
    private static final String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USERS + "("
            + COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_USER_NAME + " TEXT NOT NULL, "
            + COLUMN_USER_AGE + " INTEGER NOT NULL, "
            + COLUMN_USER_HEIGHT + " REAL NOT NULL"
            + ")";

    // Create health data table
    private static final String CREATE_HEALTH_DATA_TABLE = "CREATE TABLE " + TABLE_HEALTH_DATA + "("
            + COLUMN_HEALTH_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_HEALTH_USER_ID + " INTEGER NOT NULL, "
            + COLUMN_HEALTH_DATE + " TEXT NOT NULL, "
            + COLUMN_HEALTH_SYSTOLIC + " INTEGER, "
            + COLUMN_HEALTH_DIASTOLIC + " INTEGER, "
            + COLUMN_HEALTH_WEIGHT + " REAL, "
            + COLUMN_HEALTH_FAT_PERCENTAGE + " REAL, "
            + COLUMN_HEALTH_BMI + " REAL, "
            + "UNIQUE(" + COLUMN_HEALTH_USER_ID + ", " + COLUMN_HEALTH_DATE + "), "
            + "FOREIGN KEY(" + COLUMN_HEALTH_USER_ID + ") REFERENCES " + TABLE_USERS + "(" + COLUMN_USER_ID + ")"
            + ")";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_USERS_TABLE);
        db.execSQL(CREATE_HEALTH_DATA_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_HEALTH_DATA);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
    }

    // User operations
    public long addUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_NAME, user.getName());
        values.put(COLUMN_USER_AGE, user.getAge());
        values.put(COLUMN_USER_HEIGHT, user.getHeight());
        return db.insert(TABLE_USERS, null, values);
    }

    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_USERS + " ORDER BY " + COLUMN_USER_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                User user = new User();
                user.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_USER_ID)));
                user.setName(cursor.getString(cursor.getColumnIndex(COLUMN_USER_NAME)));
                user.setAge(cursor.getInt(cursor.getColumnIndex(COLUMN_USER_AGE)));
                user.setHeight(cursor.getDouble(cursor.getColumnIndex(COLUMN_USER_HEIGHT)));
                users.add(user);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return users;
    }

    public User getUserById(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS, null, COLUMN_USER_ID + "=?",
                new String[]{String.valueOf(userId)}, null, null, null);

        User user = null;
        if (cursor.moveToFirst()) {
            user = new User();
            user.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_USER_ID)));
            user.setName(cursor.getString(cursor.getColumnIndex(COLUMN_USER_NAME)));
            user.setAge(cursor.getInt(cursor.getColumnIndex(COLUMN_USER_AGE)));
            user.setHeight(cursor.getDouble(cursor.getColumnIndex(COLUMN_USER_HEIGHT)));
        }
        cursor.close();
        return user;
    }

    public int updateUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_NAME, user.getName());
        values.put(COLUMN_USER_AGE, user.getAge());
        values.put(COLUMN_USER_HEIGHT, user.getHeight());
        return db.update(TABLE_USERS, values, COLUMN_USER_ID + "=?",
                new String[]{String.valueOf(user.getId())});
    }

    public int deleteUser(int userId) {
        SQLiteDatabase db = this.getWritableDatabase();
        // First delete all health data for this user
        db.delete(TABLE_HEALTH_DATA, COLUMN_HEALTH_USER_ID + "=?",
                new String[]{String.valueOf(userId)});
        // Then delete the user
        return db.delete(TABLE_USERS, COLUMN_USER_ID + "=?",
                new String[]{String.valueOf(userId)});
    }

    // Health data operations
    public long addHealthData(HealthData healthData) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_HEALTH_USER_ID, healthData.getUserId());
        values.put(COLUMN_HEALTH_DATE, healthData.getDate());
        values.put(COLUMN_HEALTH_SYSTOLIC, healthData.getSystolicPressure());
        values.put(COLUMN_HEALTH_DIASTOLIC, healthData.getDiastolicPressure());
        values.put(COLUMN_HEALTH_WEIGHT, healthData.getWeight());
        values.put(COLUMN_HEALTH_FAT_PERCENTAGE, healthData.getFatPercentage());
        values.put(COLUMN_HEALTH_BMI, healthData.getBmi());
        return db.insertWithOnConflict(TABLE_HEALTH_DATA, null, values, SQLiteDatabase.CONFLICT_REPLACE);
    }

    public List<HealthData> getHealthDataByUser(int userId) {
        List<HealthData> healthDataList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_HEALTH_DATA + 
                           " WHERE " + COLUMN_HEALTH_USER_ID + "=? " +
                           " ORDER BY " + COLUMN_HEALTH_DATE + " DESC";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{String.valueOf(userId)});

        if (cursor.moveToFirst()) {
            do {
                HealthData healthData = new HealthData();
                healthData.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_HEALTH_ID)));
                healthData.setUserId(cursor.getInt(cursor.getColumnIndex(COLUMN_HEALTH_USER_ID)));
                healthData.setDate(cursor.getString(cursor.getColumnIndex(COLUMN_HEALTH_DATE)));
                healthData.setSystolicPressure(cursor.getInt(cursor.getColumnIndex(COLUMN_HEALTH_SYSTOLIC)));
                healthData.setDiastolicPressure(cursor.getInt(cursor.getColumnIndex(COLUMN_HEALTH_DIASTOLIC)));
                healthData.setWeight(cursor.getDouble(cursor.getColumnIndex(COLUMN_HEALTH_WEIGHT)));
                healthData.setFatPercentage(cursor.getDouble(cursor.getColumnIndex(COLUMN_HEALTH_FAT_PERCENTAGE)));
                healthData.setBmi(cursor.getDouble(cursor.getColumnIndex(COLUMN_HEALTH_BMI)));
                healthDataList.add(healthData);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return healthDataList;
    }

    public HealthData getHealthDataByUserAndDate(int userId, String date) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_HEALTH_DATA, null, 
                COLUMN_HEALTH_USER_ID + "=? AND " + COLUMN_HEALTH_DATE + "=?",
                new String[]{String.valueOf(userId), date}, null, null, null);

        HealthData healthData = null;
        if (cursor.moveToFirst()) {
            healthData = new HealthData();
            healthData.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_HEALTH_ID)));
            healthData.setUserId(cursor.getInt(cursor.getColumnIndex(COLUMN_HEALTH_USER_ID)));
            healthData.setDate(cursor.getString(cursor.getColumnIndex(COLUMN_HEALTH_DATE)));
            healthData.setSystolicPressure(cursor.getInt(cursor.getColumnIndex(COLUMN_HEALTH_SYSTOLIC)));
            healthData.setDiastolicPressure(cursor.getInt(cursor.getColumnIndex(COLUMN_HEALTH_DIASTOLIC)));
            healthData.setWeight(cursor.getDouble(cursor.getColumnIndex(COLUMN_HEALTH_WEIGHT)));
            healthData.setFatPercentage(cursor.getDouble(cursor.getColumnIndex(COLUMN_HEALTH_FAT_PERCENTAGE)));
            healthData.setBmi(cursor.getDouble(cursor.getColumnIndex(COLUMN_HEALTH_BMI)));
        }
        cursor.close();
        return healthData;
    }

    public int updateHealthData(HealthData healthData) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_HEALTH_SYSTOLIC, healthData.getSystolicPressure());
        values.put(COLUMN_HEALTH_DIASTOLIC, healthData.getDiastolicPressure());
        values.put(COLUMN_HEALTH_WEIGHT, healthData.getWeight());
        values.put(COLUMN_HEALTH_FAT_PERCENTAGE, healthData.getFatPercentage());
        values.put(COLUMN_HEALTH_BMI, healthData.getBmi());
        return db.update(TABLE_HEALTH_DATA, values, COLUMN_HEALTH_ID + "=?",
                new String[]{String.valueOf(healthData.getId())});
    }

    public int deleteHealthData(int healthDataId) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_HEALTH_DATA, COLUMN_HEALTH_ID + "=?",
                new String[]{String.valueOf(healthDataId)});
    }
} 
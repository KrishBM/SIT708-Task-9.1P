package com.example.task91p.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Database Info
    private static final String DATABASE_NAME = "lostAndFoundDatabase";
    private static final int DATABASE_VERSION = 1;

    // Table Names
    private static final String TABLE_ITEMS = "items";

    // Item Table Columns
    private static final String KEY_ITEM_ID = "id";
    private static final String KEY_ITEM_TYPE = "type"; // "Lost" or "Found"
    private static final String KEY_ITEM_NAME = "name";
    private static final String KEY_ITEM_PHONE = "phone";
    private static final String KEY_ITEM_DESCRIPTION = "description";
    private static final String KEY_ITEM_DATE = "date";
    private static final String KEY_ITEM_LOCATION = "location";
    private static final String KEY_ITEM_LATITUDE = "latitude";
    private static final String KEY_ITEM_LONGITUDE = "longitude";

    // Singleton instance
    private static DatabaseHelper sInstance;

    public static synchronized DatabaseHelper getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new DatabaseHelper(context.getApplicationContext());
        }
        return sInstance;
    }

    private DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_ITEMS_TABLE = "CREATE TABLE " + TABLE_ITEMS +
                "(" +
                KEY_ITEM_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                KEY_ITEM_TYPE + " TEXT NOT NULL," +
                KEY_ITEM_NAME + " TEXT NOT NULL," +
                KEY_ITEM_PHONE + " TEXT NOT NULL," +
                KEY_ITEM_DESCRIPTION + " TEXT NOT NULL," +
                KEY_ITEM_DATE + " TEXT NOT NULL," +
                KEY_ITEM_LOCATION + " TEXT NOT NULL," +
                KEY_ITEM_LATITUDE + " REAL," +
                KEY_ITEM_LONGITUDE + " REAL" +
                ")";

        db.execSQL(CREATE_ITEMS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            // Drop older table if existed
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_ITEMS);
            // Create tables again
            onCreate(db);
        }
    }

    // CRUD Operations

    // Add new item
    public long addItem(String type, String name, String phone, String description, String date, String location, double latitude, double longitude) {
        SQLiteDatabase db = getWritableDatabase();
        
        ContentValues values = new ContentValues();
        values.put(KEY_ITEM_TYPE, type);
        values.put(KEY_ITEM_NAME, name);
        values.put(KEY_ITEM_PHONE, phone);
        values.put(KEY_ITEM_DESCRIPTION, description);
        values.put(KEY_ITEM_DATE, date);
        values.put(KEY_ITEM_LOCATION, location);
        values.put(KEY_ITEM_LATITUDE, latitude);
        values.put(KEY_ITEM_LONGITUDE, longitude);
        
        // Insert row
        long id = db.insert(TABLE_ITEMS, null, values);
        db.close();
        return id;
    }
    
    // For backward compatibility
    public long addItem(String type, String name, String phone, String description, String date, String location) {
        // Default coordinates (0,0) if not provided
        return addItem(type, name, phone, description, date, location, 0, 0);
    }

    // Get all items
    public Cursor getAllItems() {
        SQLiteDatabase db = getReadableDatabase();
        return db.query(
                TABLE_ITEMS,
                null,
                null,
                null,
                null,
                null,
                KEY_ITEM_ID + " DESC"
        );
    }

    // Delete item by id
    public int deleteItem(long id) {
        SQLiteDatabase db = getWritableDatabase();
        int rowsAffected = db.delete(TABLE_ITEMS, KEY_ITEM_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
        return rowsAffected;
    }

    // Get item by id
    public Cursor getItemById(long id) {
        SQLiteDatabase db = getReadableDatabase();
        return db.query(
                TABLE_ITEMS,
                null,
                KEY_ITEM_ID + " = ?",
                new String[]{String.valueOf(id)},
                null,
                null,
                null
        );
    }
} 
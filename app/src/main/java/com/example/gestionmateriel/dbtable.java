package com.example.gestionmateriel;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class dbtable extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "ManagementDatabase.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_USERS = "users";
    private static final String COLUMN_FULLNAME = "fullname";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_PASSWORD = "password";

    private static final String TABLE_MATERIAL = "material";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_DESCRIPTION = "description";
    private static final String COLUMN_SERIAL_NUMBER = "serial_number";

    public dbtable(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createUserTable = "CREATE TABLE " + TABLE_USERS + " (" +
                COLUMN_EMAIL + " TEXT PRIMARY KEY, " +
                COLUMN_FULLNAME + " TEXT, " +
                COLUMN_PASSWORD + " TEXT)";
        db.execSQL(createUserTable);

        String createMaterialTable = "CREATE TABLE " + TABLE_MATERIAL + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAME + " TEXT, " +
                COLUMN_DESCRIPTION + " TEXT, " +
                COLUMN_SERIAL_NUMBER + " TEXT)";
        db.execSQL(createMaterialTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MATERIAL);
        onCreate(db);
    }

    public boolean insertUser(String fullName, String email, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_FULLNAME, fullName);
        values.put(COLUMN_EMAIL, email);
        values.put(COLUMN_PASSWORD, password);

        long result = db.insert(TABLE_USERS, null, values);
        return result != -1;
    }

    public boolean insertMaterial(String name, String description, String serialNumber) {
        if (name.isEmpty() || description.isEmpty() || serialNumber.isEmpty()) {
            return false;
        }
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, name);
        values.put(COLUMN_DESCRIPTION, description);
        values.put(COLUMN_SERIAL_NUMBER, serialNumber);

        long result = db.insert(TABLE_MATERIAL, null, values);
        return result != -1;
    }

    public boolean checkUser(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USERS + " WHERE " + COLUMN_EMAIL + "=? AND " + COLUMN_PASSWORD + "=?", new String[]{email, password});
        boolean isUserExists = cursor.getCount() > 0;
        cursor.close();
        return isUserExists;
    }

    public Cursor readAllMaterials() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_MATERIAL, null);
    }

    public boolean updateMaterial(String id, String name, String description, String serialNumber) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, name);
        values.put(COLUMN_DESCRIPTION, description);
        values.put(COLUMN_SERIAL_NUMBER, serialNumber);

        int rowsUpdated = db.update(TABLE_MATERIAL, values, COLUMN_ID + " = ?", new String[]{id});
        return rowsUpdated > 0;
    }

    public boolean deleteMaterial(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.delete(TABLE_MATERIAL, COLUMN_ID + "=?", new String[]{id});
        return result != -1;
    }
}


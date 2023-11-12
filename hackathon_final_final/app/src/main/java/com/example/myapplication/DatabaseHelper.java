package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.HashMap;

public class DatabaseHelper extends SQLiteOpenHelper {

    private String databaseName = "Hackaton.db";

    public DatabaseHelper(@Nullable Context context) {
        super(context, "Hackaton4.db", null, 1);
    }
    @Override
    public void onCreate(SQLiteDatabase MyDatabase) {

        MyDatabase.execSQL("create Table users(id INTEGER primary key AUTOINCREMENT, username TEXT, parola TEXT)");
        MyDatabase.execSQL("create Table activities(id INTEGER primary key AUTOINCREMENT, id_user INT, data TEXT, categorie TEXT, ore TEXT)");


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop Table if exists users");
        db.execSQL("drop Table if exists activities");
    }

    public boolean addUser(String username, String parola) {
        SQLiteDatabase MyDatabase = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("username", username);
        cv.put("parola", parola);
        long result = MyDatabase.insert("users", null, cv);


        if (result == -1) {
            return false;
        }
        else {
            return true;
        }
    }

    public boolean checkUsernameAndPassword(String username, String password) {
        SQLiteDatabase MyDatabase = this.getReadableDatabase();
        Cursor cursor = MyDatabase.rawQuery("SELECT * from users where username = ? and parola = ?", new String[]{username, password});

        if (cursor.getCount() > 0) {
            return true;
        }
        else return false;
    }

    public boolean insertActivity(int id_user, String categorie, String data, String ore) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("id_user", id_user);
        cv.put("categorie", categorie);
        cv.put("data", data);
        cv.put("ore", ore);
        long result = db.insert("activities", null, cv);

        if (result == -1) {
            return false;
        }
        else {
            return true;
        }
    }

    public HashMap<String, String[]> findByCategory(int id_user, String categorie) {
        HashMap<String, String[]> result = new HashMap<>();

        SQLiteDatabase db = this.getReadableDatabase();


        String[] projection = {
                "data",
                "ore"
        };


        String selection = "id_user = ? AND categorie = ?";
        String[] selectionArgs = {String.valueOf(id_user), categorie};


        Cursor cursor = db.query(
                "activities",       // The table to query
                projection,         // The columns to return
                selection,          // The columns for the WHERE clause
                selectionArgs,      // The values for the WHERE clause
                null,               // don't group the rows
                null,               // don't filter by row groups
                null                // don't sort the order
        );


        while (cursor.moveToNext()) {
            String date = cursor.getString(cursor.getColumnIndexOrThrow("data"));
            String nrOfHours = cursor.getString(cursor.getColumnIndexOrThrow("ore"));


            if (result.containsKey(date)) {

                String[] existingHours = result.get(date);
                String[] newHours = new String[existingHours.length + 1];
                System.arraycopy(existingHours, 0, newHours, 0, existingHours.length);
                newHours[existingHours.length] = nrOfHours;
                result.put(date, newHours);
            } else {

                result.put(date, new String[]{nrOfHours});
            }
        }


        cursor.close();
        db.close();

        return result;
    }

    public int getIdOfUser(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT id FROM users WHERE username = ?", new String[]{username});
        int id_user = -1;
        while(cursor.moveToNext()) {
            id_user = cursor.getInt(cursor.getColumnIndexOrThrow("id"));

            if (id_user > -1) {
                return id_user;
            }

        }
        return id_user;
    }








}

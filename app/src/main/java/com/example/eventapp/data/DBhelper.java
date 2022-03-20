package com.example.eventapp.data;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class DBhelper extends SQLiteOpenHelper {

    public static final String DBNAME = "Event.db";
    Context con;

    public DBhelper(@Nullable Context context) {
        super(context, "Event.db", null, 1);
        con = context;
    }

    @Override
    public void onCreate(SQLiteDatabase MyDB) {
        MyDB.execSQL("create table if not exists student (id INTEGER primary key autoincrement, name TEXT, username TEXT , email TEXT, password TEXT) ");
        MyDB.execSQL("create table if not exists organizer (id INTEGER primary key autoincrement, name TEXT, username TEXT , email TEXT, password TEXT) ");
        MyDB.execSQL("create table if not exists event (id INTEGER primary key autoincrement, name TEXT, description TEXT, organizer_id INT) ");
    }

    @Override
    public void onUpgrade(SQLiteDatabase MyDB, int i, int i1) {

    }

    public Boolean insertStudent(String name, String username, String email, String password, String type) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("username", username);
        contentValues.put("email", email);
        contentValues.put("password", password);
        Toast.makeText(con, "usertype is "+type, Toast.LENGTH_SHORT).show();

        if(type.equals("student")) {
            long result = MyDB.insert("student", null, contentValues);
            Toast.makeText(con, "inserting in student...", Toast.LENGTH_SHORT).show();
            if (result == -1) return false;
            else return true;
        }
        else if(type.equals("organizer")) {
            long result = MyDB.insert("organizer", null, contentValues);
            if (result == -1) return false;
            else return true;
        }

        return false;

    }

    public void checkUser(String username, String password, String type) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        if(type.equals("student")) {
            Cursor cursor = MyDB.rawQuery("select * from student where username = ? and password = ?", new String[] {username, password});
            if(cursor.getCount() > 0) {
                Toast.makeText(con, " Student exists..", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(con, studentHome.class);
                con.startActivity(intent);
            }
            else {
                Toast.makeText(con, " Student does not exists..", Toast.LENGTH_SHORT).show();
            }

        }
       else if(type.equals("organizer")) {
            Cursor cursor = MyDB.rawQuery("select * from organizer where username = ? and password = ?", new String[] {username, password});
            if(cursor.getCount() > 0) {
                Toast.makeText(con, " Organizer exists..", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(con, organizerHome.class);
                con.startActivity(intent);
            }
            else {
                Toast.makeText(con, " Organizer does not exists..", Toast.LENGTH_SHORT).show();
            }
        }

    }
}



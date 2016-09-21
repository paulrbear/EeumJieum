package dev.erica.hyunji.eeumjieum;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Erica on 2016-08-07.
 */
public class DBHelper extends SQLiteOpenHelper {
    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version){
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL("CREATE TABLE USER (_id INTEGER PRIMARY KEY AUTOINCREMENT, compNum TEXT, email TEXT, pwd TEXT, mode INTEGER);");
        db.execSQL("CREATE TABLE ROOM (_id INTEGER PRIMARY KEY AUTOINCREMENT, userName TEXT, roomType TEXT, img INTEGER);");

        db.execSQL("CREATE TABLE WORKER (_id INTEGER PRIMARY KEY AUTOINCREMENT, email TEXT, name TEXT, room TEXT, img INTEGER);");
        db.execSQL("CREATE TABLE PARENT (_id INTEGER PRIMARY KEY AUTOINCREMENT, email TEXT, name TEXT, room TEXT, img INTEGER);");

        db.execSQL("CREATE TABLE OBSERVE (_id INTEGER PRIMARY KEY AUTOINCREMENT, objectname TEXT, objectroom TEXT, writer TEXT, writerroom TEXT, mood INTEGER, activity INTEGER, sleep INTEGER, day TEXT, dayorder INTEGER, content TEXT, photo TEXT);");
        db.execSQL("CREATE TABLE OBSERVECOMMENT (_id INTEGER PRIMARY KEY AUTOINCREMENT, articlekey INTEGER, day TEXT, time TEXT, comorder INTEGER, writer TEXT, content TEXT);");

        db.execSQL("CREATE TABLE PROGRAM (_id INTEGER PRIMARY KEY AUTOINCREMENT, objectname TEXT, writer TEXT, writerroom TEXT, day TEXT, dayorder INTEGER, title TEXT, content TEXT, photo TEXT);");
        db.execSQL("CREATE TABLE PROGRAMCOMMENT (_id INTEGER PRIMARY KEY AUTOINCREMENT, articlekey INTEGER, day TEXT, time TEXT, comorder INTEGER, writer TEXT, content TEXT);");

        db.execSQL("CREATE TABLE WORKREPORT (_id INTEGER PRIMARY KEY AUTOINCREMENT, objectroom TEXT, day TEXT, normalperson TEXT, outperson TEXT, hospitalperson TEXT, etcperson TEXT, programtxt TEXT);");
        db.execSQL("CREATE TABLE PERSONALWORKREPORT (_id INTEGER PRIMARY KEY AUTOINCREMENT, reportkey INTEGER, objectname TEXT, objectroom TEXT, objectimg INTEGER, status INTEGER, content TEXT, meal1 INTEGER, meal2 INTEGER, meal3 INTEGER);");

        db.execSQL("CREATE TABLE SCHEDULE (_id INTEGER PRIMARY KEY AUTOINCREMENT, objectroom TEXT, writer TEXT, writerroom TEXT, day TEXT, dayorder INTEGER, time TEXT, title TEXT, location TEXT, content TEXT, photo TEXT);");
        db.execSQL("CREATE TABLE SCHEDULECOMMENT (_id INTEGER PRIMARY KEY AUTOINCREMENT, articlekey INTEGER, day TEXT, time TEXT, comorder INTEGER, writer TEXT, content TEXT);");

        db.execSQL("CREATE TABLE NOTICE (_id INTEGER PRIMARY KEY AUTOINCREMENT, objectroom TEXT, writer TEXT, writerroom TEXT, day TEXT, dayorder INTEGER, title TEXT, content TEXT, photo TEXT);");
        db.execSQL("CREATE TABLE NOTICECOMMENT (_id INTEGER PRIMARY KEY AUTOINCREMENT, articlekey INTEGER, day TEXT, time TEXT, comorder INTEGER, writer TEXT, content TEXT);");

        db.execSQL("CREATE TABLE DIET (_id INTEGER PRIMARY KEY AUTOINCREMENT, day TEXT, morning TEXT, afternoon TEXT, night TEXT, photo1 INTEGER, photo2 INTEGER, photo3 INTEGER);");
        db.execSQL("CREATE TABLE ALBUM (_id INTEGER PRIMARY KEY AUTOINCREMENT, objectname TEXT, writer TEXT, writerroom TEXT, day TEXT, dayorder INTEGER, title TEXT, content TEXT, photo TEXT);");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){

    }

}

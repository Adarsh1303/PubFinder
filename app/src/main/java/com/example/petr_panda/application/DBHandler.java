package com.example.petr_panda.application;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by petr_panda on 11/7/17.
 */
public class DBHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 9;
    private static final String DATABASE_NAME = "places.db";
    public static final String TABLE_PLACES = "places";
    public  final String COLUMN_ID = "_id";
    public  final String COLUMN_PLACENAME = "placename";
    public  final String COLUMN_PLACEADDR = "placeaddr";
    public  final String COLUMN_PLACETYPE = "placetype";
    public  final String COLUMN_PLACERATING = "placerating";
    public  final String COLUMN_PLACENUMBER = "placenumber";

    public DBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context,DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        String query = "CREATE TABLE " + TABLE_PLACES + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_PLACENAME + " TEXT, " +
                COLUMN_PLACEADDR + " TEXT, "+
                COLUMN_PLACETYPE + " TEXT, " +
                COLUMN_PLACERATING + " TEXT, " +
                COLUMN_PLACENUMBER + " TEXT" +
                ");";

        sqLiteDatabase.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_PLACES);
        onCreate(sqLiteDatabase);


    }

    public void addplace(Place place){

        ContentValues values = new ContentValues();
        values.put(COLUMN_PLACENAME, place.getPlace_name());
        values.put(COLUMN_PLACEADDR, place.getPlace_address());
        values.put(COLUMN_PLACETYPE, place.getPlace_type());
        values.put(COLUMN_PLACERATING, place.getPlace_rating());
        values.put(COLUMN_PLACENUMBER, place.getPlace_number());
        SQLiteDatabase db = getWritableDatabase();
        db.insert(TABLE_PLACES,null,values);
        db.close();
    }

    public Cursor getall()
    {
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_PLACES + ";";

        Cursor c = db.rawQuery(query,null);
        c.moveToFirst();
        return c;

    }

    public Cursor get_place_detail(String name)
    {
        SQLiteDatabase db = getWritableDatabase();
        String[] params = new String[] {name};
        String query = "SELECT * FROM " + TABLE_PLACES + " WHERE " + COLUMN_PLACENAME + "=?" ;

        Cursor c = db.rawQuery(query,params);
        c.moveToFirst();
        return c;
    }
}

package com.niza.app.givder.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import com.niza.app.givder.App;

public class GivderMessageDB extends SQLiteOpenHelper {
    public static final String DATABASE_NAME="GivderMessageDB.db";
    public static final int DATABASE_VERSION=1;
    public static final String TABLE_NAME="GivderMessageDB";


    public static final  String messageFrom="messageFrom";
    public static final  String time="time";
    public static final  String messageTo="messageTo";
    public static final  String message="message";
    public static final  String type="type";
    public static final  String IsViewed="IsViewed";
    public static final  String Lat="Lat";
    public static final  String Lon="Lon";
    public static final  String TimeExpiration="TimeExpiration";
    public static final  String Description="Description";
    public static final  String Plates="Plates";
    public static final  String Color="Color";

/*
    private  static final String DATABASE_CREATE = "CREATE VIRTUAL TABLE "+ TABLE_NAME +
            "  USING fts3("
            + ContributionId  + " text not null UNIQUE, "
            + PhoneNumber    + " text not null, "
            + Type + " text not null, "
            + IsMatch + " text not null, "
            + IsAccepted + " text not null, "
            + IsViewed + " text not null, "
            + Lat + " integer not null, "
            + Lon + " integer not null, "
            + TimeExpiration + " integer not null, "
            + Description + " text not null, "
            + Plates + " text not null, "
            + Color + " text not null);";*/

      public GivderMessageDB(Context context, String name, SQLiteDatabase.CursorFactory factory,
                             int version) {
        super(context, name, factory, version);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        App.Log(getClass().getSimpleName()+" Creating all the tables");
        try {
     //       db.execSQL(DATABASE_CREATE);


        } catch(SQLiteException ex) {
            App.Log("Create table exception "+ ex.getMessage());
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public String getTableName() {
        return TABLE_NAME;
    }


}

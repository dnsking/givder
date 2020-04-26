package com.niza.app.givder.db.helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import com.niza.app.givder.db.GivderMessageDB;
import com.niza.app.givder.db.GivderMessageDB;
import com.niza.app.givder.networking.actions.GiverMessageNetwork;
import com.niza.app.givder.networking.actions.UserNetworkAction;

import java.util.ArrayList;

public class GivderMessageDBHelper {


    private SQLiteDatabase db;
    private Context context;

    private GivderMessageDB dbhelper;
    public GivderMessageDBHelper(Context c){
        context = c;
        this.dbhelper = new GivderMessageDB(c, GivderMessageDB.DATABASE_NAME, null,
                GivderMessageDB.DATABASE_VERSION);
    }
    public Context getContext(){
        return context;
    }
    public void close()
    {
        db.close();
    }
    public SQLiteDatabase getDB(){
        return db;
    }
    public void setDB(SQLiteDatabase db){
        this.db =db;
    }
    public SQLiteOpenHelper getDbHelper(){
        return dbhelper;
    }
    public void open() throws SQLiteException
    {
        try {
            db = dbhelper.getWritableDatabase();
        } catch(SQLiteException ex) {
            db = dbhelper.getReadableDatabase();
        }
    }
    public long insertEntry(ContentValues cv){
        try{
            return db.insert(dbhelper.getTableName(), null, cv);
        }
        catch(SQLiteException ex){;
            return -1;
        }
    }


    public Cursor query()
    {
        return query(null, null, null, null, null, null);
    }
    public Cursor query( String[] columns, String selection,
                         String[] selectionArgs, String groupBy, String having,
                         String orderBy)
    {

        Cursor c = db.query(dbhelper.getTableName(),null, null,
                null, null, null, null);
        return c;
    }

    public void deleteEntry(int id){
        db.delete(dbhelper.getTableName(), GivderMessageDB.time+"="+id, null);
    }
    public GiverMessageNetwork[] queryAllTo(String numberTo){

        Cursor cursor = query(null,GivderMessageDB.messageTo+"="+numberTo,null, null, null,null);

        ArrayList<GiverMessageNetwork> giverMessageNetworks = new ArrayList<>();
        //  cursor.moveToFirst();
        while (cursor.moveToNext()) {
            giverMessageNetworks.add(new GiverMessageNetwork(

                    cursor.getString(cursor.getColumnIndex(GivderMessageDB.messageFrom)),

                    cursor.getString(cursor.getColumnIndex(GivderMessageDB.time)),

                    cursor.getString(cursor.getColumnIndex(GivderMessageDB.messageTo)),


                    cursor.getString(cursor.getColumnIndex(GivderMessageDB.message)),
                    cursor.getString(cursor.getColumnIndex(GivderMessageDB.type))));


        }
        cursor.close();
        return giverMessageNetworks.toArray(new  GiverMessageNetwork[]{});
    }
    public GiverMessageNetwork[] queryAll(){

        Cursor cursor = query( );

        ArrayList<GiverMessageNetwork> giverMessageNetworks = new ArrayList<>();
        //  cursor.moveToFirst();
        while (cursor.moveToNext()) {
            giverMessageNetworks.add(new GiverMessageNetwork(

                    cursor.getString(cursor.getColumnIndex(GivderMessageDB.messageFrom)),

                    cursor.getString(cursor.getColumnIndex(GivderMessageDB.time)),

                    cursor.getString(cursor.getColumnIndex(GivderMessageDB.messageTo)),


                    cursor.getString(cursor.getColumnIndex(GivderMessageDB.message)),
                    cursor.getString(cursor.getColumnIndex(GivderMessageDB.type))));


        }
        cursor.close();
        return giverMessageNetworks.toArray(new  GiverMessageNetwork[]{});
    }
    public ContentValues updateEntry(GiverMessageNetwork giverMessageNetwork){
        try{



            ContentValues cv = new ContentValues();

            cv.put(GivderMessageDB.message,giverMessageNetwork.getMessage());
            cv.put(GivderMessageDB.messageFrom,giverMessageNetwork.getFrom());
            cv.put(GivderMessageDB.messageTo,giverMessageNetwork.getTo());
            cv.put(GivderMessageDB.type,giverMessageNetwork.getType());
            cv.put(GivderMessageDB.time,giverMessageNetwork.getTime());
            getDB().update(GivderMessageDB.TABLE_NAME,  cv,"Where "+GivderMessageDB.time+"="+giverMessageNetwork.getTime()
                    ,null);
            return cv;
        }
        catch(SQLiteException ex) {
            return null;
        }

    }
    public void insertEntry(GiverMessageNetwork giverMessageNetworks[]){

        db.beginTransaction();

        try{

            for (GiverMessageNetwork giverMessageNetwork : giverMessageNetworks) {
                ContentValues cv = new ContentValues();

                cv.put(GivderMessageDB.message,giverMessageNetwork.getMessage());
                cv.put(GivderMessageDB.messageFrom,giverMessageNetwork.getFrom());
                cv.put(GivderMessageDB.messageTo,giverMessageNetwork.getTo());
                cv.put(GivderMessageDB.type,giverMessageNetwork.getType());
                cv.put(GivderMessageDB.time,giverMessageNetwork.getTime());
                db.insert(GivderMessageDB.TABLE_NAME, null, cv);
            }

            db.setTransactionSuccessful();

        }
        finally  {

            db.endTransaction();
        }

    }
    public ContentValues insertEntry(GiverMessageNetwork giverMessageNetwork){


        try{

            ContentValues cv = new ContentValues();
            cv.put(GivderMessageDB.message,giverMessageNetwork.getMessage());
            cv.put(GivderMessageDB.messageFrom,giverMessageNetwork.getFrom());
            cv.put(GivderMessageDB.messageTo,giverMessageNetwork.getTo());
            cv.put(GivderMessageDB.type,giverMessageNetwork.getType());
            cv.put(GivderMessageDB.time,giverMessageNetwork.getTime());
            getDB().insert(GivderMessageDB.TABLE_NAME, null, cv);
            return cv;
        }
        catch(SQLiteException ex) {
            return null;
        }

    }
}

package com.niza.app.givder.db.helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import com.niza.app.givder.db.GivderContentDB;
import com.niza.app.givder.networking.actions.UserNetworkAction;

import java.util.ArrayList;

public class GivderContentDBHelper {


    private SQLiteDatabase db;
    private Context context;

    private GivderContentDB dbhelper;
    public GivderContentDBHelper(Context c){
        context = c;
        this.dbhelper = new GivderContentDB(c, GivderContentDB.DATABASE_NAME, null,
                GivderContentDB.DATABASE_VERSION);
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
        db.delete(dbhelper.getTableName(), GivderContentDB.PhoneNumber+"="+id, null);
    }
    public UserNetworkAction[] queryAll(){
        Cursor cursor = query( );
        ArrayList<UserNetworkAction> userNetworkActions = new ArrayList<>();
        //  cursor.moveToFirst();
        while (cursor.moveToNext()) {
            userNetworkActions.add(new UserNetworkAction(

                    cursor.getString(cursor.getColumnIndex(GivderContentDB.UserName)),

                    cursor.getString(cursor.getColumnIndex(GivderContentDB.ContributionId)),

                    cursor.getString(cursor.getColumnIndex(GivderContentDB.Type)),

                    cursor.getString(cursor.getColumnIndex(GivderContentDB.Lat)),

                    cursor.getString(cursor.getColumnIndex(GivderContentDB.Lon)),

                    cursor.getLong(cursor.getColumnIndex(GivderContentDB.TimeExpiration)),

                    cursor.getString(cursor.getColumnIndex(GivderContentDB.Description)),

                    cursor.getString(cursor.getColumnIndex(GivderContentDB.Plates)),

                    cursor.getString(cursor.getColumnIndex(GivderContentDB.Color)),

                    Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex(GivderContentDB.IsMatch))),

                    Boolean.parseBoolean( cursor.getString(cursor.getColumnIndex(GivderContentDB.IsViewed))) ,

                    Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex(GivderContentDB.IsAccepted)))));


        }
        cursor.close();
        return userNetworkActions.toArray(new UserNetworkAction[]{});
    }
    public ContentValues updateEntry(UserNetworkAction userNetworkAction,
    boolean isMatch,  boolean isViewed,  boolean isAccepted){
        try{



            ContentValues cv = new ContentValues();

            cv.put(GivderContentDB.ContributionId,userNetworkAction.getContributionId());
            cv.put(GivderContentDB.PhoneNumber,userNetworkAction.getPhoneNumber());
            cv.put(GivderContentDB.UserName,userNetworkAction.getUserName());
            cv.put(GivderContentDB.Type,userNetworkAction.getType());
            cv.put(GivderContentDB.Lon,userNetworkAction.getLon());
            cv.put(GivderContentDB.Lat,userNetworkAction.getLat());
            cv.put(GivderContentDB.TimeExpiration,userNetworkAction.getTimeExpiration());
            cv.put(GivderContentDB.Description,userNetworkAction.getDescription());
            cv.put(GivderContentDB.Plates,userNetworkAction.getPlates());
            cv.put(GivderContentDB.Color,userNetworkAction.getColor());
            cv.put(GivderContentDB.IsMatch,isMatch);
            cv.put(GivderContentDB.IsViewed,isViewed);
            cv.put(GivderContentDB.IsAccepted,isAccepted);
            getDB().update(GivderContentDB.TABLE_NAME,  cv,"Where "+GivderContentDB.ContributionId+"="+userNetworkAction.getContributionId()
                    ,null);
            return cv;
        }
        catch(SQLiteException ex) {
            return null;
        }

    }
    public void insertEntry(UserNetworkAction userNetworkActions[]){

        db.beginTransaction();

        try{

            for (UserNetworkAction userNetworkAction : userNetworkActions) {
                ContentValues cv = new ContentValues();

                cv.put(GivderContentDB.ContributionId,userNetworkAction.getContributionId());
                cv.put(GivderContentDB.PhoneNumber,userNetworkAction.getPhoneNumber());
                cv.put(GivderContentDB.UserName,userNetworkAction.getUserName());
                cv.put(GivderContentDB.Type,userNetworkAction.getType());
                cv.put(GivderContentDB.Lon,userNetworkAction.getLon());
                cv.put(GivderContentDB.Lat,userNetworkAction.getLat());
                cv.put(GivderContentDB.TimeExpiration,userNetworkAction.getTimeExpiration());
                cv.put(GivderContentDB.Description,userNetworkAction.getDescription());
                cv.put(GivderContentDB.Plates,userNetworkAction.getPlates());
                cv.put(GivderContentDB.Color,userNetworkAction.getColor());
                cv.put(GivderContentDB.IsMatch,userNetworkAction.isMatch());
                cv.put(GivderContentDB.IsViewed,userNetworkAction.isViewed());
                cv.put(GivderContentDB.IsAccepted,userNetworkAction.isAccepted());
                db.insert(GivderContentDB.TABLE_NAME, null, cv);
            }

            db.setTransactionSuccessful();

        }
        finally  {

            db.endTransaction();
        }

    }
    public ContentValues insertEntry(UserNetworkAction userNetworkAction){


        try{

            ContentValues cv = new ContentValues();
            cv.put(GivderContentDB.ContributionId,userNetworkAction.getContributionId());
            cv.put(GivderContentDB.PhoneNumber,userNetworkAction.getPhoneNumber());
            cv.put(GivderContentDB.UserName,userNetworkAction.getUserName());
            cv.put(GivderContentDB.Type,userNetworkAction.getType());
            cv.put(GivderContentDB.Lon,userNetworkAction.getLon());
            cv.put(GivderContentDB.Lat,userNetworkAction.getLat());
            cv.put(GivderContentDB.TimeExpiration,userNetworkAction.getTimeExpiration());
            cv.put(GivderContentDB.Description,userNetworkAction.getDescription());
            cv.put(GivderContentDB.Plates,userNetworkAction.getPlates());
            cv.put(GivderContentDB.Color,userNetworkAction.getColor());
            cv.put(GivderContentDB.IsMatch,userNetworkAction.isMatch());
            cv.put(GivderContentDB.IsViewed,userNetworkAction.isViewed());
            cv.put(GivderContentDB.IsAccepted,userNetworkAction.isAccepted());
            getDB().insert(GivderContentDB.TABLE_NAME, null, cv);
            return cv;
        }
        catch(SQLiteException ex) {
            return null;
        }

    }
}

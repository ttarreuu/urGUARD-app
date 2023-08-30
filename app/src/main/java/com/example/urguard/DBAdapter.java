package com.example.urguard;

import android.app.DownloadManager;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBAdapter extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "userDB.db";
    private static final String TABLE_USERS = "users";
    public static final String COLOUMN_ID = "id";
    public static final String COLOUMN_NAME = "name";
    public static final String COLOUMN_CONTACT_1 = "contact_1";
    public static final String COLOUMN_CONTACT_2 = "contact_2";
    public static final String COLOUMN_CONTACT_3 = "contact_3";
    public static final String COLOUMN_MESSAGE = "message";
    SQLiteDatabase db;

    public DBAdapter(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USERS + "(" + COLOUMN_ID +
                " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLOUMN_NAME + " TEXT NOT NULL, "
                + COLOUMN_CONTACT_1 + " TEXT NOT NULL, "
                + COLOUMN_CONTACT_2 + " TEXT, "
                + COLOUMN_CONTACT_3 + " TEXT, "
                + COLOUMN_MESSAGE + " TEXT NOT NULL)";
        db.execSQL(CREATE_USERS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
        Log.w("DBAdapter", "Upgrading database from version " + oldVersion + " tc " + newVersion + ". This will destroy all old data");
    }

    @Override
    public synchronized void close() {
        super.close();
    }

    public void insertUser(user user){
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLOUMN_NAME, user.getName());
        contentValues.put(COLOUMN_CONTACT_1, user.getContact_1());
        contentValues.put(COLOUMN_CONTACT_2, user.getContact_2());
        contentValues.put(COLOUMN_CONTACT_3, user.getContact_3());
        contentValues.put(COLOUMN_MESSAGE, user.getMessage());

        db.insert(TABLE_USERS, null, contentValues);
    }

    public user getUser(){
        String query = "SELECT * FROM " + TABLE_USERS;

        Cursor cursor = db.rawQuery(query, null);
        user user = null;
        if(cursor.moveToFirst()){
            user = new user();
            user.setId(Integer.parseInt(cursor.getString(0)));
            user.setName(cursor.getString(1));
            user.setContact_1(cursor.getString(2));
            user.setContact_2(cursor.getString(3));
            user.setContact_3(cursor.getString(4));
            user.setMessage(cursor.getString(5));
        }
        cursor.close();
        return user;
    }

    public Cursor getAllUser(){
        return db.query(TABLE_USERS, new String[]{COLOUMN_ID, COLOUMN_NAME, COLOUMN_CONTACT_1, COLOUMN_CONTACT_2, COLOUMN_CONTACT_3},
                null, null, null, null, null);
    }

    public boolean updateUser(String oldName, String newContact_1, String  newContact_2, String newContact_3, String newMessage){
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLOUMN_NAME, oldName);
        contentValues.put(COLOUMN_CONTACT_1, newContact_1);
        contentValues.put(COLOUMN_CONTACT_2, newContact_2);
        contentValues.put(COLOUMN_CONTACT_3, newContact_3);
        contentValues.put(COLOUMN_MESSAGE, newMessage);

        return db.update(TABLE_USERS, contentValues, COLOUMN_NAME+"=?", new String[]{oldName}) > 0;
    }

    public boolean deleteUser(String name){
        String query = "SELECT * FROM " + TABLE_USERS + " WHERE " + COLOUMN_NAME + " =\"" + name + "\"";
        Cursor cursor = db.rawQuery(query, null);
        boolean result = false;
        if(cursor.moveToFirst()){
            int userID = cursor.getInt(0);
            db.delete(TABLE_USERS, COLOUMN_ID + " =?", new String[]{String.valueOf(userID)});
            result = true;
        }
        return result;
    }

}

package com.example.cgpadraft1java;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;


public class tmnDB extends SQLiteOpenHelper{
    private static final String dbname = "Users.db";
    private static final int db_version = 1;
    private static final String TName = "users_Table";

    private static final String cl1_id = "id";
    private static final String cl2_name = "name";
    private static final String cl3_email = "email";
    private static final String cl4_password = "password";


    public tmnDB (Context context){
        super(context,dbname,null,db_version);
    }


    public void onCreate(SQLiteDatabase tdb){
        String ttbl="CREATE TABLE " + TName + "(" + cl1_id
                + " INTEGER PRIMARY KEY," + cl2_name + " TEXT," + cl3_email + " TEXT," +
                cl4_password + " TEXT" + ")";
        tdb.execSQL(ttbl);
    }

    @Override
    public void onUpgrade(SQLiteDatabase tdb, int oldVersion, int
            newVersion) {
        //drop the table if it exists
        tdb.execSQL("DROP TABLE IF EXISTS " + TName);
        onCreate(tdb);
    }


    public void addUser(Users tuser)
    {
// getting db instance for writing the user
        SQLiteDatabase tdb=this.getWritableDatabase();
        ContentValues tvalues=new ContentValues();
// tvalues.put(User_id,tuser.getId());
        tvalues.put(cl2_name,tuser.getName());
        tvalues.put(cl3_email,tuser.getEmail());
        tvalues.put(cl4_password,tuser.getPassword());

//inserting row
        tdb.insert(TName, null, tvalues);
//close the database to avoid any leak
        tdb.close();
    }


    public boolean checkUser(String em, String pass)
    {
        SQLiteDatabase tdb=this.getReadableDatabase();
        Cursor cursor=tdb.query(TName, new String[]{cl1_id},
                cl3_email + "=? AND "+ cl4_password +"=?", new String[]{em,pass},null,null,null);
        boolean valid = cursor.getCount()>0;
        cursor.close();
        tdb.close();
        return valid;
    }




}

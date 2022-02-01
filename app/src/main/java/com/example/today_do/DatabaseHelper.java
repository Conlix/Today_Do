package com.example.today_do;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String ID = "ID";
    private static final String TOPIC = "TOPIC";
    private static final String TASK = "TASK";
    private static final String DETAILS = "DETAILS";
    private static final String CREATIONTIME = "CREATIONTIME";
    private static final String DEADLINE = "DEADLINE";
    private static final String TODAY = "TODAY";

    private String name;
    Context mcontext;

    public DatabaseHelper(@Nullable Context context, @Nullable String topic) {
        super(context, topic, null, 1);
        mcontext = context;
        name = topic;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "create table '" + name + "' (" + ID + " INTEGER PRIMARY KEY, " +
                TOPIC + " TEXT," + TASK + " TEXT," + DETAILS + " TEXT," +
                CREATIONTIME + " DOUBLE," + DEADLINE + " DOUBLE," + TODAY + " INTEGER)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+ name);
        onCreate(db);
    }

    public void addTask(Task task){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(ID,task.getId());
        contentValues.put(TOPIC,task.getTopic());
        contentValues.put(TASK,task.getTask());
        contentValues.put(DETAILS,task.getDetails());
        contentValues.put(CREATIONTIME,task.getCreations_time());
        contentValues.put(DEADLINE,task.getDeadline());
        contentValues.put(TODAY,task.isToday());
        long insert = sqLiteDatabase.insert(name,null,contentValues);
        sqLiteDatabase.close();
    }

    public void editTask(Task task){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        int today = 0;
        if(task.isToday()){ today = 1; }
        String queryString = "UPDATE '"+ name+"' SET "+ TASK +" = '"+task.getTask()+"', "+TODAY+" = "+today+", "+DETAILS+" = '"+task.getDetails()+"', "+ DEADLINE+" = "+ task.getDeadline()+ " WHERE "+ ID+ " = "+ task.getId();
        sqLiteDatabase.execSQL(queryString);
    }

    public ArrayList<Task> getAllTasks(){
        ArrayList<Task> returnlist = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        Cursor cursor = sqLiteDatabase.rawQuery("select * from '"+name+"'",null);
        cursor.moveToFirst();
        boolean today;
        while (!cursor.isAfterLast()){
            if (cursor.getInt(6) == 0){
                today = false;
            }else{
                today = true;
            }
            returnlist.add(new Task(cursor.getInt(0),cursor.getString(1),cursor.getString(2),cursor.getString(3),cursor.getDouble(4),cursor.getDouble(5),today));
            cursor.moveToNext();
        }
        cursor.close();
        sqLiteDatabase.close();
        return returnlist;
    }

    public void deliteOne(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        String queryString = "DELETE FROM '"+ name+"' WHERE "+ ID+ " = "+ id;
        db.execSQL(queryString);
        db.close();
    }

    public void DeleteDatabase(){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.execSQL("delete from '"+ name+"'");
    }
}

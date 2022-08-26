package com.example.mandarinlearning.data.local;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.mandarinlearning.utils.Const;

import java.io.IOException;

/**
 * Created by macos on 19,July,2022
 */
public class Database extends SQLiteOpenHelper {
    public Database(Context context) {
        super(context, Const.Database.DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //db.close();
        //master
        String sql = "create table word(wordId integer primary key autoincrement, simplified text,rank integer,hsk integer,favorite integer)";
        db.execSQL(sql);
        //entry
        sql = "create table entry(entryId integer primary key autoincrement,wordOwnerId interger, traditional text,pinyin text,definition text)";
        db.execSQL(sql);
        //example
        sql = "create table example(exampleId integer primary key autoincrement,wordOwnerId interger, hanzi text,pinyin text,translation text,audio text)";
        db.execSQL(sql);
        //search_history
        sql = "create table search_history(historyId integer primary key autoincrement, simplified text,pinyin text,definition text)";
        db.execSQL(sql);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    private void createDataBase() throws IOException {
        this.getReadableDatabase();
        this.close();
    }
    
    private boolean checkIfDBExists() {
        // File dbFile = new File(DB_COMPLETE_PATH);
        //return dbFile.exists();
        return false;
    }

}

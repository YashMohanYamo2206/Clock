package com.yash.clock.DatabaseHandler;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class database extends SQLiteOpenHelper {

    public database(@Nullable Context context) {
        super(context, "alarms", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_SCORE_TABLE = "CREATE TABLE " +
                alarm_data_contract.alarm_data_contract_columns.TABLE_NAME + " (" +
                alarm_data_contract.alarm_data_contract_columns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                alarm_data_contract.alarm_data_contract_columns.TIME + " TEXT NOT NULL, " +
                alarm_data_contract.alarm_data_contract_columns.IsAlarmOn + " TEXT NOT NULL, " +
                alarm_data_contract.alarm_data_contract_columns.TIME_STAMP + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                ");";
        db.execSQL(SQL_CREATE_SCORE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + alarm_data_contract.alarm_data_contract_columns.TABLE_NAME);
        onCreate(db);
    }
}

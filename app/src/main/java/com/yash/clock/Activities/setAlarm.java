package com.yash.clock.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.yash.clock.Alerts_and_Notifications.AlertReceiver;
import com.yash.clock.DatabaseHandler.alarm_data_contract;
import com.yash.clock.DatabaseHandler.database;
import com.yash.clock.Fragments.timePicker;
import com.yash.clock.R;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Objects;

public class setAlarm extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener {
    Calendar calendar;
    SQLiteDatabase mDatabase;
    String time;
    CheckBox monday, tuesday, wednesday, thursday, friday, saturday, sunday;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_aler__dialog);
        database dbHelper = new database(this);
        mDatabase = dbHelper.getReadableDatabase();


        findViewById(R.id.setTime).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment timePicker = new timePicker();
                timePicker.show(getSupportFragmentManager(), "time picker");
            }
        });
        findViewById(R.id.setAlarm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(setAlarm.this, "" + time, Toast.LENGTH_SHORT).show();
                set_Alarm(calendar, getAllItems().getCount());
            }
        });
    }

    private void set_Alarm(Calendar calendar, int req_code) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlertReceiver.class);
        intent.putExtra("req_code",req_code);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, req_code, intent, 0);
        assert alarmManager != null;
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);
        time = DateFormat.getTimeInstance(DateFormat.SHORT).format(calendar.getTime());
        ContentValues contentValues = new ContentValues();
        contentValues.put(alarm_data_contract.alarm_data_contract_columns.TIME, time);
        contentValues.put(alarm_data_contract.alarm_data_contract_columns.IsAlarmOn, "true");
        contentValues.put(alarm_data_contract.alarm_data_contract_columns.REQ_CODE, String.valueOf(getAllItems().getCount() + 1));
        mDatabase.insert(alarm_data_contract.alarm_data_contract_columns.TABLE_NAME, null, contentValues);
        TextView alarm = findViewById(R.id.alarm_time_tv);
        alarm.setText(time);
    }

    private Cursor getAllItems() {
        return mDatabase.query(
                alarm_data_contract.alarm_data_contract_columns.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                alarm_data_contract.alarm_data_contract_columns.TIME_STAMP + " DESC"
        );
    }

}
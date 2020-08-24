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
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.yash.clock.Alerts_and_Notifications.AlertReceiver;
import com.yash.clock.DatabaseHandler.alarm_data_contract;
import com.yash.clock.DatabaseHandler.database;
import com.yash.clock.Fragments.alarm;
import com.yash.clock.Fragments.timePicker;
import com.yash.clock.R;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Objects;

public class setAlarm extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener {
    Calendar calendar;
    public MediaPlayer mediaPlayer;
    SQLiteDatabase mDatabase;
    String time;
    int req_code;
    CheckBox monday, tuesday, wednesday, thursday, friday, saturday, sunday;
    Boolean isEdit;
    int hour, min;
    int mp_ID = R.raw.alarm;
    RadioGroup radioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_aler__dialog);
        radioGroup = findViewById(R.id.groupRadio);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (group.findViewById(checkedId).getId()) {
                    case R.id.radio_id1:
                        if (mediaPlayer == null) {
                            mediaPlayer = MediaPlayer.create(setAlarm.this, R.raw.alarm);
                            mediaPlayer.start();
                        } else {
                            mediaPlayer.stop();
                            mediaPlayer.release();
                            mediaPlayer = MediaPlayer.create(setAlarm.this, R.raw.alarm);
                            mediaPlayer.start();
                        }
                        Log.d("onCheckedChanged: ", "1");
                        mp_ID = R.raw.alarm;
                        break;
                    case R.id.radio_id2:
                        if (mediaPlayer == null) {
                            mediaPlayer = MediaPlayer.create(setAlarm.this, R.raw.motivational);
                            mediaPlayer.start();
                        } else {
                            mediaPlayer.stop();
                            mediaPlayer.release();
                            mediaPlayer = MediaPlayer.create(setAlarm.this, R.raw.motivational);
                            mediaPlayer.start();
                        }
                        Log.d("onCheckedChanged: ", "2");
                        mp_ID = R.raw.motivational;
                        break;
                    case R.id.radio_id3:
                        if (mediaPlayer == null) {
                            mediaPlayer = MediaPlayer.create(setAlarm.this, R.raw.metal);
                            mediaPlayer.start();
                        } else {
                            mediaPlayer.stop();
                            mediaPlayer.release();
                            mediaPlayer = MediaPlayer.create(setAlarm.this, R.raw.metal);
                            mediaPlayer.start();
                        }
                        Log.d("onCheckedChanged: ", "3");
                        mp_ID = R.raw.metal;
                        break;
                }
            }
        });
        database dbHelper = new database(this);
        mDatabase = dbHelper.getReadableDatabase();
        Intent intent = getIntent();
        isEdit = intent.getBooleanExtra("isEdit", false);
        time = intent.getStringExtra("time");
        hour = intent.getIntExtra("hour", -1);
        min = intent.getIntExtra("min", -1);
        req_code = intent.getIntExtra("req_code", -1);
        Log.d("onCreate: ", req_code + "");
        if (time != null) {
            TextView alarm = findViewById(R.id.alarm_time_tv);
            alarm.setText(time);
        }
        findViewById(R.id.setTime).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isEdit) {
                    DialogFragment timePicker = new timePicker();
                    timePicker.show(getSupportFragmentManager(), "time picker");
                } else {
                    DialogFragment timePicker = new timePicker(hour, min);
                    timePicker.show(getSupportFragmentManager(), "time picker");
                }
            }
        });
        findViewById(R.id.setAlarm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (!isEdit) {
                        ContentValues contentValues = new ContentValues();
                        contentValues.put(alarm_data_contract.alarm_data_contract_columns.TIME, time);
                        contentValues.put(alarm_data_contract.alarm_data_contract_columns.IsAlarmOn, "true");
                        contentValues.put(alarm_data_contract.alarm_data_contract_columns.REQ_CODE, String.valueOf(getAllItems().getCount() + 1));
                        mDatabase.insert(alarm_data_contract.alarm_data_contract_columns.TABLE_NAME, null, contentValues);
                        Toast.makeText(setAlarm.this, "ALARM SET FOR : " + time, Toast.LENGTH_SHORT).show();
                        set_Alarm(calendar, getAllItems().getCount());
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                finish();
                            }
                        }, 50);
                        alarm.adapter.notifyDataSetChanged();
                    } else {
                        ContentValues contentValues = new ContentValues();
                        contentValues.put(alarm_data_contract.alarm_data_contract_columns.TIME, time);
                        mDatabase.update(alarm_data_contract.alarm_data_contract_columns.TABLE_NAME, contentValues, alarm_data_contract.alarm_data_contract_columns.REQ_CODE + "=?", new String[]{String.valueOf(req_code)});
                        set_Alarm(calendar, req_code);
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                finish();
                            }
                        }, 50);
                        alarm.adapter.swapCursor(getAllItems());
                    }
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    Toast.makeText(setAlarm.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void set_Alarm(Calendar calendar, int req_code) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlertReceiver.class);
        intent.putExtra("req_code", req_code);
        intent.putExtra("mp_ID", mp_ID);
        if (calendar.before(Calendar.getInstance())) {
            calendar.add(Calendar.DATE, 1);
        }
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
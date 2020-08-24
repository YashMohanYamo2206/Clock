package com.yash.clock.Fragments;


import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TimePicker;
import android.widget.Toast;

import com.yash.clock.Activities.setAlarm;
import com.yash.clock.Adapter.alarms_adapter;
import com.yash.clock.Alerts_and_Notifications.AlertReceiver;
import com.yash.clock.DatabaseHandler.alarm_data_contract;
import com.yash.clock.DatabaseHandler.database;
import com.yash.clock.R;

import java.text.Format;
import java.util.Calendar;
import java.util.Objects;


public class alarm extends Fragment implements alarms_adapter.onItemClickListener {
    Button addAlarm;
    RecyclerView recyclerView;
    public static alarms_adapter adapter;
    RecyclerView.LayoutManager layoutManager;
    SQLiteDatabase mDatabase;

    public alarm() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_alarm, container, false);
        addAlarm = view.findViewById(R.id.add_alarm);
        addAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), setAlarm.class);
                startActivity(intent);
            }
        });
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        init_recyclerView();
    }

    private void init_recyclerView() {
        database dbHelper = new database(getContext());
        mDatabase = dbHelper.getReadableDatabase();
        recyclerView = Objects.requireNonNull(getView()).findViewById(R.id.alarm_recycler_view);
        adapter = new alarms_adapter(getAllItems());
        adapter.setOnItemClickListener(this);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);
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


    private void setAlarm(Calendar calendar, int req_code) {
        AlarmManager alarmManager = (AlarmManager) Objects.requireNonNull(getContext()).getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(getContext(), AlertReceiver.class);
        if (calendar.before(Calendar.getInstance())) {
            calendar.add(Calendar.DATE, 1);
        }
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), req_code, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        assert alarmManager != null;
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
    }

    private void cancelAlarm(int req_code) {
        AlarmManager alarmManager = (AlarmManager) Objects.requireNonNull(getContext()).getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(getContext(), AlertReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), req_code, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        assert alarmManager != null;
        alarmManager.cancel(pendingIntent);
    }

    @Override
    public void onSwitchClick(int position, boolean isChecked, String time, @SuppressLint("UseSwitchCompatOrMaterialCode") CompoundButton s) {
        Calendar calendar = Calendar.getInstance();
        int hour, min;
        ContentValues contentValues = new ContentValues();
        if (s.isPressed()) {
            if (!isChecked) {
                Toast.makeText(getContext(), "ALARM FOR " + time + " cancelled", Toast.LENGTH_SHORT).show();
                contentValues.put(alarm_data_contract.alarm_data_contract_columns.IsAlarmOn, "false");
                mDatabase.update(alarm_data_contract.alarm_data_contract_columns.TABLE_NAME, contentValues,
                        alarm_data_contract.alarm_data_contract_columns.REQ_CODE + "=?",
                        new String[]{String.valueOf(getAllItems().getCount() - position)});
                cancelAlarm(getAllItems().getCount() - position);

            } else {
                contentValues.put(alarm_data_contract.alarm_data_contract_columns.IsAlarmOn, "true");
                mDatabase.update(alarm_data_contract.alarm_data_contract_columns.TABLE_NAME, contentValues,
                        alarm_data_contract.alarm_data_contract_columns.REQ_CODE + "=?",
                        new String[]{String.valueOf(getAllItems().getCount() - position)});
                if (time.substring(2, 3).equals(":")) {
                    hour = Integer.parseInt(time.substring(0, 2));
                    min = (Integer.parseInt(time.substring(3, 4)) == 0) ? (Integer.parseInt(time.substring(4, 5))) : (Integer.parseInt(time.substring(3, 5)));
                    if (!DateFormat.is24HourFormat(getContext())) {
                        try {
                            if (time.substring(6, 7).equals("A")) {
                                calendar.set(Calendar.AM_PM, Calendar.AM);
                            } else {
                                calendar.set(Calendar.AM_PM, Calendar.PM);
                            }
                        } catch (Exception ignored) {

                        }
                        calendar.set(Calendar.HOUR, hour);
                        calendar.set(Calendar.MINUTE, min);
                    }
                } else {
                    hour = Integer.parseInt(time.substring(0, 1));
                    min = (Integer.parseInt(time.substring(2, 3)) == 0) ? (Integer.parseInt(time.substring(3, 4))) : (Integer.parseInt(time.substring(2, 4)));
                    if (!DateFormat.is24HourFormat(getContext())) {
                        try {
                            if (time.substring(5, 6).equals("A")) {
                                calendar.set(Calendar.AM_PM, Calendar.AM);
                            } else {
                                calendar.set(Calendar.AM_PM, Calendar.PM);
                            }
                        } catch (Exception ignored) {

                        }
                    }
                    calendar.set(Calendar.HOUR, hour);
                    calendar.set(Calendar.MINUTE, min);
                }
                setAlarm(calendar, getAllItems().getCount() - position);
            }
        }
    }

    @Override
    public void onEditClick(int position, String time) {
        int hour, min;
        boolean isAM = false;
        if (time.substring(2, 3).equals(":")) {
            hour = Integer.parseInt(time.substring(0, 2));
            min = (Integer.parseInt(time.substring(3, 4)) == 0) ? (Integer.parseInt(time.substring(4, 5))) : (Integer.parseInt(time.substring(3, 5)));
            if (!DateFormat.is24HourFormat(getContext())) {
                try {
                    isAM = time.substring(6, 7).equals("A");
                } catch (Exception ignored) {

                }
            }
        } else {
            hour = Integer.parseInt(time.substring(0, 1));
            min = (Integer.parseInt(time.substring(2, 3)) == 0) ? (Integer.parseInt(time.substring(3, 4))) : (Integer.parseInt(time.substring(2, 4)));
            if (!DateFormat.is24HourFormat(getContext())) {
                try {
                    isAM = time.substring(5, 6).equals("A");
                } catch (Exception ignored) {

                }
            }
        }
        Intent intent = new Intent(getActivity(), setAlarm.class);
        intent.putExtra("req_code",getAllItems().getCount() - position);
        intent.putExtra("isEdit", true);
        intent.putExtra("hour",hour);
        intent.putExtra("min",min);
        intent.putExtra("time",time);
        startActivity(intent);
    }
}
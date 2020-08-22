package com.yash.clock.Fragments;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TimePicker;
import android.widget.Toast;


import com.yash.clock.Adapter.alarms_adapter;
import com.yash.clock.Alerts_and_Notifications.AlertReceiver;
import com.yash.clock.DatabaseHandler.alarm_data_contract;
import com.yash.clock.DatabaseHandler.database;
import com.yash.clock.R;

import java.text.DateFormat;
import java.util.Calendar;


public class alarm extends Fragment implements TimePickerDialog.OnTimeSetListener {
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
                DialogFragment timePicker = new timePicker();
                timePicker.show(getChildFragmentManager(), "time picker");
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
        recyclerView = getView().findViewById(R.id.alarm_recycler_view);
        adapter = new alarms_adapter(getAllItems());
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

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

        Calendar calendar;

        calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);
        String time = DateFormat.getTimeInstance(DateFormat.SHORT).format(calendar.getTime());

        ContentValues contentValues = new ContentValues();
        contentValues.put(alarm_data_contract.alarm_data_contract_columns.TIME, time);
        contentValues.put(alarm_data_contract.alarm_data_contract_columns.IsAlarmOn, "true");
        mDatabase.insert(alarm_data_contract.alarm_data_contract_columns.TABLE_NAME, null, contentValues);


        adapter.swapCursor(getAllItems());
        adapter.notifyDataSetChanged();
        setAlarm(calendar);
        Toast.makeText(getContext(), "ALARM SET FOR : " +time, Toast.LENGTH_SHORT).show();

    }

    private void setAlarm(Calendar calendar) {
        AlarmManager alarmManager =  (AlarmManager) getContext().getSystemService(getContext().ALARM_SERVICE);
        Intent intent = new Intent(getContext(), AlertReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), 1, intent, 0);
        //if (calendar.before(Calendar.getInstance())) {
          //  calendar.add(Calendar.DATE, 1);
        //}
        assert alarmManager != null;
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
    }
}
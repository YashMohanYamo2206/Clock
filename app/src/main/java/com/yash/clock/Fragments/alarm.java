package com.yash.clock.Fragments;


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


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TimePicker;
import android.widget.Toast;

import com.yash.clock.Activities.setAlarm;
import com.yash.clock.Adapter.alarms_adapter;
import com.yash.clock.Alerts_and_Notifications.AlertReceiver;
import com.yash.clock.DatabaseHandler.alarm_data_contract;
import com.yash.clock.DatabaseHandler.database;
import com.yash.clock.R;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Objects;


public class alarm extends Fragment implements  alarms_adapter.onItemClickListener {
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
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), req_code, intent, 0);
        assert alarmManager != null;
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
    }


    @Override
    public void onSwitchClick(int position, boolean isChecked) {
        if (!isChecked) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(alarm_data_contract.alarm_data_contract_columns.IsAlarmOn, "false");
            mDatabase.update(alarm_data_contract.alarm_data_contract_columns.TABLE_NAME, contentValues,
                    alarm_data_contract.alarm_data_contract_columns.REQ_CODE + "=?",
                    new String[]{String.valueOf(getAllItems().getCount() - position)});

        } else {
            ContentValues contentValues = new ContentValues();
            contentValues.put(alarm_data_contract.alarm_data_contract_columns.IsAlarmOn, "true");
            mDatabase.update(alarm_data_contract.alarm_data_contract_columns.TABLE_NAME, contentValues,
                    alarm_data_contract.alarm_data_contract_columns.REQ_CODE + "=?",
                    new String[]{String.valueOf(getAllItems().getCount() - position)});
        }
    }

    @Override
    public void onEditClick(int position) {

    }

    @Override
    public void onLongPress(int position) {
        final int pos = position;
        Toast.makeText(getContext(), ""+position, Toast.LENGTH_SHORT).show();
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(Objects.requireNonNull(getContext()));
        builder.setMessage("").setCancelable(
                true).setPositiveButton("DELETE",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mDatabase.delete(alarm_data_contract.alarm_data_contract_columns.TABLE_NAME,
                                alarm_data_contract.alarm_data_contract_columns.REQ_CODE+"=?",new String[]{String.valueOf(getAllItems().getCount()-pos)});
                        adapter.swapCursor(getAllItems());
                        adapter.notifyDataSetChanged();
                        dialog.cancel();
                    }
                }).setNegativeButton("CANCEL",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }
}
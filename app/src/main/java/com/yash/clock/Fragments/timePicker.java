package com.yash.clock.Fragments;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.yash.clock.DatabaseHandler.alarm_data_contract;
import com.yash.clock.DatabaseHandler.database;

import java.util.Calendar;

public class timePicker extends DialogFragment {
    Calendar calendar = Calendar.getInstance();
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MONTH);
        return new TimePickerDialog(getContext(),(TimePickerDialog.OnTimeSetListener) getParentFragment(), hour, minute, DateFormat.is24HourFormat(getActivity()));
    }
}

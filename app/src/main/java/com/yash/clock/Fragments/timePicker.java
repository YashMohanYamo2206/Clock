package com.yash.clock.Fragments;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;


import java.util.Calendar;

public class timePicker extends DialogFragment {
    Calendar calendar = Calendar.getInstance();
    int hour;
    int minute;


    public timePicker(){
        hour = calendar.get(Calendar.HOUR_OF_DAY);
        minute = calendar.get(Calendar.MINUTE);
    }

    public timePicker(int hour, int minute) {
        this.hour = hour;
        this.minute = minute;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        return new TimePickerDialog(getContext(), (TimePickerDialog.OnTimeSetListener) getContext(), hour, minute, DateFormat.is24HourFormat(getActivity()));
    }
}

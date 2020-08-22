package com.yash.clock.Fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Chronometer;
import android.widget.ImageButton;

import com.yash.clock.R;

import java.util.Objects;

@SuppressLint({"DefaultLocale", "SetTextI18n", "UseCompatLoadingForDrawables"})
public class stopWatch extends Fragment {

    Chronometer chronometer;
    ImageButton start, stop;


    private boolean isResume;
    Handler handler;
    long tMilliSec, tStart, tBuff, tUpdate = 0L;
    int sec, min, milliSec;

    public stopWatch() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_stop_watch, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        handler = new Handler();
        chronometer = Objects.requireNonNull(getView()).findViewById(R.id.chronometer);
        start = getView().findViewById(R.id.btn_playPause);
        stop = getView().findViewById(R.id.btn_stop);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isResume) {
                    tStart = SystemClock.uptimeMillis();
                    handler.postDelayed(runnable, 0);
                    chronometer.start();
                    isResume = true;
                    stop.setVisibility(View.GONE);
                    start.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_pause_24, null));
                } else {
                    tBuff += tMilliSec;
                    handler.removeCallbacks(runnable);
                    chronometer.stop();
                    isResume = false;
                    stop.setVisibility(View.VISIBLE);
                    start.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_play_arrow_24, null));
                }
            }
        });
        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isResume) {
                    start.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_play_arrow_24, null));
                    tMilliSec = 0L;
                    tStart = 0L;
                    tBuff = 0L;
                    tUpdate = 0L;
                    sec = 0;
                    min = 0;
                    milliSec = 0;
                    chronometer.setText("00:00:00");
                    stop.setVisibility(View.GONE);
                }
            }
        });
    }

    public Runnable runnable = new Runnable() {
        @Override
        public void run() {
            tMilliSec = SystemClock.uptimeMillis() - tStart;
            tUpdate = tBuff + tMilliSec;
            sec = (int) (tUpdate / 1000);
            min = (int) (sec / 60);
            sec = sec % 60;
            milliSec = (int) (tUpdate % 100);
            chronometer.setText(String.format("%02d", min) + ":" + String.format("%02d", sec) + ":" + String.format("%02d", milliSec));
            handler.postDelayed(this, 60);
        }
    };
}
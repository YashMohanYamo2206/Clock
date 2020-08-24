package com.yash.clock.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.yash.clock.Alerts_and_Notifications.AlertReceiver;
import com.yash.clock.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.Vector;

public class after_notification_tap extends AppCompatActivity {
    Vector<Integer> v_factors = new Vector<>();
    Vector<Integer> v_notFactors = new Vector<>();
    Random number = new Random();
    int et_randNum;
    Button generateFactors, num1, num2, num3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_page);
        generateFactors = findViewById(R.id.btn_generateFactors);
        num1 = findViewById(R.id.btn_num1);
        num2 = findViewById(R.id.btn_num2);
        num3 = findViewById(R.id.btn_num3);
    }

    @SuppressLint("SetTextI18n")
    public void clickGenerateFactors(View view) {

        et_randNum = number.nextInt(10000);
        TextView textView = findViewById(R.id.tv);
        textView.setText(textView.getText()+String.valueOf(et_randNum));
        for (int i = 2; i <= et_randNum; i++) {
            if (et_randNum % i == 0) {
                v_factors.add(i);
            } else {
                v_notFactors.add(i);
            }
        }
        v_factors.trimToSize();
        v_notFactors.trimToSize();
        int num_rand1 = number.nextInt(v_notFactors.size());
        int num_rand2 = number.nextInt(v_notFactors.size());
        int num_rand3 = number.nextInt(v_factors.size());
        ArrayList<Integer> rand = new ArrayList<>();
        rand.add(v_factors.get(num_rand3));
        rand.add(v_notFactors.get(num_rand1));
        v_notFactors.remove(v_notFactors.get((num_rand1)));
        rand.add(v_notFactors.get(num_rand2));
        Collections.shuffle(rand);
        Collections.shuffle(rand);
        num1.setVisibility(View.VISIBLE);
        num1.setText(String.valueOf(rand.get(0)));
        num2.setVisibility(View.VISIBLE);
        num2.setText(String.valueOf(rand.get(1)));
        num3.setVisibility(View.VISIBLE);
        num3.setText(String.valueOf(rand.get(2)));
        num1.setEnabled(true);
        num2.setEnabled(true);
        num3.setEnabled(true);
        generateFactors.setEnabled(false);
        v_factors.clear();
        v_notFactors.clear();
    }

    public void Click(View view) {
        Button b = (Button) view;
        if (et_randNum% Integer.parseInt(String.valueOf(b.getText())) == 0) {
            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            assert manager != null;
            manager.cancel(1);
            Toast.makeText(this, "right answer!!!", Toast.LENGTH_SHORT).show();
            num1.setEnabled(false);
            num2.setEnabled(false);
            num3.setEnabled(false);
            AlertReceiver.mediaPlayer.release();
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    finish();
                }
            },100);
        }
        else {
            Toast.makeText(this, "wrong answer!!!", Toast.LENGTH_SHORT).show();
        }
    }
}